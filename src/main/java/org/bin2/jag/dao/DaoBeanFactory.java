package org.bin2.jag.dao;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bin2.jag.dao.query.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Spring FactoryBean used to build the Dao for the interface specified 
 * <ul>
 *   <li> sessionFactory property is mandatory
 *   <li> dao class property is mandatory
 *   <li> persitentClass is optional, if none is provided :
 *        the factoryBean try to 
 *        compute it automatically by getting the type specified on the Dao (User for
 *        the previous example). If no persistent class is set and it cannot
 *        automatically fetch the type the factory will throw an exception.
 * </ul>
 **/
public class DaoBeanFactory implements FactoryBean<Dao<?, ?>> {
    private boolean singleton;
    /**
     * the dao class to implement
     */
    private Class<? extends Dao<?, ? extends Serializable>> dao;

    /**
     * optional persistentClass
     * if not provided will check the parametized type of the DAO
     */
    private Class<?> persistentClass;

    /**
     * the session factory
     */
    private SessionFactory sessionFactory;

    /**
     * @return the dao class to implement
     **/
    public Class<? extends Dao<?, ? extends Serializable>> getDao() {
        return this.dao;
    }

    @Override
    public Dao<?, ?> getObject() throws Exception {
        if (this.persistentClass == null) {
            tryingExtractPersistentType();
        }
        final DaoProxyHandler handler = new DaoProxyHandler(
                this.sessionFactory, this.dao, this.persistentClass, buildQueryContexts(dao));
        return Dao.class
                .cast(Proxy.newProxyInstance(Thread.currentThread()
                        .getContextClassLoader(), new Class<?>[]{this.dao},
                        handler));
    }

    private Map<Method, QueryContext> buildQueryContexts(Class<?> daoClass) {
        Map<Method, QueryContext> contexts = Maps.newHashMap();
        for (Method m : daoClass.getMethods()) {
            contexts.put(m, buildContextForMethod(m, daoClass));
        }
        return contexts;
    }

    private QueryContext buildContextForMethod(Method m, Class<?> daoClass) {
        final QueryHandler queryHandler = buildQueryHandler(m, daoClass);
        final ResultHandler result = buildResultHandler(m);
        List<ParameterHandler> parameterHandlers = Lists.newArrayList();
        final Annotation[][] annotations = m.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            parameterHandlers.add(buildParameterHandler(annotations[i]));
        }
        return new QueryContext(queryHandler, result, parameterHandlers);

    }

    private ParameterHandler buildParameterHandler(Annotation[] annotations) {
        final NamedParameter annot = getParameterAnnotation(annotations,
                NamedParameter.class);
        ParameterHandler parameterHandler = null;
        if (annot != null) {
            parameterHandler = new NamedParameterHandler(annot.value());
        } else if (getParameterAnnotation(annotations, FetchSize.class) != null) {
            parameterHandler = new FetchSizeParameterHandler();
        } else if (getParameterAnnotation(annotations, FirstResult.class) != null) {
            parameterHandler = new FirstResultParameterHandler();
        } else {
            // IndexedParam not implemented ... maybe latter if i find a good strategy ....
            //  parameterHandler= new IndexedParameterHandler(i);
        }
        return parameterHandler==null?NoActionParameterHandler.INSTANCE:parameterHandler;
    }

    private QueryHandler buildQueryHandler(Method m, Class<?> daoClass) {
        final StringBuilder queryName = new StringBuilder(daoClass
                .getSimpleName());
        queryName.append(".").append(m.getName());
        final Query q = m
                .getAnnotation(Query.class);
        final QueryHandler queryHandler;
        if (q == null) {
            queryHandler = new NamedQueryHandler(queryName.toString());
        } else {
            queryHandler = new BasicQueryHandler(q.value());
        }
        return queryHandler;
    }

    private ResultHandler buildResultHandler(Method m) {
        final ResultHandler result;
        if (m.getReturnType().isAssignableFrom(List.class)) {
            result = new ListResultHandler();
        } else if (m.getReturnType().isAssignableFrom(Iterator.class)) {
            result = new IteratorResultHandler();
        } else {
            result = new UniqueResultHandler();
        }
        return result;
    }

    private <T extends Annotation> T getParameterAnnotation(
            final Annotation[] annots,  final Class<T> annoClass) {
        final T p;
        T found = null;
        for (final Annotation a : annots) {
            if (a.annotationType().isAssignableFrom(annoClass)) {
                found = annoClass.cast(a);
            }
        }
        if (found != null) {
            p = found;
        } else {
            p = null;
        }

        return p;
    }

    @Override
    public Class<? extends Dao<?, ? extends Serializable>> getObjectType() {
        return this.dao;
    }

    /**
     * @return the sessionFactory used for the created Dao
     **/
    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    @Override
    public boolean isSingleton() {
        return this.singleton;
    }

    /**
     * @param dao the dao class to implement
     **/
    public void setDao(final Class<? extends Dao<?, ? extends Serializable>> dao) {
        this.dao = dao;
    }

    /**
     * the persistent clas managed by the created Dao 
     * for a <code>UserDao<User, Long></code> it would be <code>User</code>,
     * the property is optionnal, if not provided the factoryBean try to 
     * compute it automatically by getting the type specified on the Dao (User for
     * the previous example). If no persistent class is set and it cannot
     * automatically fetch the type the factory will throw an exception.
     * @param persistentClass the persistent clas managed by the created Dao
     **/
    public void setPersistent(final Class<?> persistentClass) {
        this.persistentClass = persistentClass;
    }

    /**
     * @param sessionFactory the sessionFactory used by the dao
     **/
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @param singleton true to have a singleton @see #isSingleton()
     **/
    public void setSingleton(final boolean singleton) {
        this.singleton = singleton;
    }

    private void tryingExtractPersistentType() {
        Preconditions.checkArgument(Dao.class.isAssignableFrom(dao),"cannot proxy a non Dao interface "+ dao.getName());
        // try to detect the type of the dao
        final Type[] types = dao.getGenericInterfaces();
        for (final Type type : dao.getGenericInterfaces()) {
            if (type instanceof ParameterizedType) {
                final ParameterizedType p = (ParameterizedType) type;
                final Class<?> rtype = (Class<?>) p.getRawType();
                if (Dao.class.isAssignableFrom(rtype)) {
                    Type t = p.getActualTypeArguments()[0];
                    if (t instanceof Class) {
                        this.persistentClass = (Class<?>) t;
                    }  else {
                        throw new IllegalArgumentException("cannot automatically resolve persistentClass, please set the property manually, type not define "+ t);
                    }
                    break;
                }
            }
        }
    }
}
