package org.bin2.jag.dao;

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

public class DaoBeanFactory implements FactoryBean<Dao<?, ?>> {
    private boolean singleton;
    /**
     * the dao class to implements
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
        final StringBuilder queryName = new StringBuilder(daoClass
                .getSimpleName());
        queryName.append(".").append(m.getName());
        final org.bin2.jag.dao.Query q = m
                .getAnnotation(org.bin2.jag.dao.Query.class);
        final QueryHandler queryHandler;
        if (q == null) {
            queryHandler = new NamedQueryHandler(queryName.toString());
        } else {
            queryHandler = new BasicQueryHandler(q.value());
        }

        final ResultHandler result;
        if (m.getReturnType().isAssignableFrom(List.class)) {
            result = new ListResultHandler();
        } else if (m.getReturnType().isAssignableFrom(Iterator.class)) {
            result = new IteratorResultHandler();
        } else {
            result = new UniqueResultHandler();
        }

        List<ParameterHandler> parameterHandlers = Lists.newArrayList();
        final Annotation[][] annotations = m.getParameterAnnotations();
        int idxCmpt = 0;
        for (int i = 0; i < annotations.length; i++) {
            final NamedParameter annot = getParameterAnnotation(annotations, i,
                    NamedParameter.class);
            ParameterHandler parameterHandler = null;
            if (annot != null) {
                parameterHandler = new NamedParameterHandler(annot.value());
            } else if (getParameterAnnotation(annotations, i, FetchSize.class) != null) {
                parameterHandler = new FetchSizeParameterHandler();
            } else if (getParameterAnnotation(annotations, i, FirstResult.class) != null) {
                parameterHandler = new FirstResultParameterHandler();
            } else {
                //  parameterHandler= new IndexedParameterHandler(idxCmpt);
                idxCmpt++;
            }
            parameterHandlers.add(parameterHandler);
        }
        return new QueryContext(queryHandler, result, parameterHandlers);

    }

    private <T extends Annotation> T getParameterAnnotation(
            final Annotation[][] annots, final int i, final Class<T> annoClass) {
        final T p;
        if (i < annots.length && annots[i] != null) {
            T found = null;
            for (final Annotation a : annots[i]) {
                if (a.annotationType().isAssignableFrom(annoClass)) {
                    found = annoClass.cast(a);
                }
            }
            if (found != null) {
                p = found;
            } else {
                p = null;
            }
        } else {
            p = null;
        }

        return p;
    }

    @Override
    public Class<? extends Dao<?, ? extends Serializable>> getObjectType() {
        return this.dao;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    @Override
    public boolean isSingleton() {
        return this.singleton;
    }

    public void setDao(final Class<? extends Dao<?, ? extends Serializable>> dao) {
        this.dao = dao;
    }

    public void setPersistent(final Class<?> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setSingleton(final boolean singleton) {
        this.singleton = singleton;
    }

    private void tryingExtractPersistentType() {
        // try to detect the type of the dao
        final Type[] types = dao.getGenericInterfaces();
        if (types != null) {
            for (final Type type : dao.getGenericInterfaces()) {
                if (type instanceof ParameterizedType) {
                    final ParameterizedType p = (ParameterizedType) type;
                    final Class<?> rtype = (Class<?>) p.getRawType();
                    if (Dao.class.isAssignableFrom(rtype)) {
                        this.persistentClass = (Class<?>) p.getActualTypeArguments()[0];
                        break;
                    }
                }
            }
        }
        if (this.persistentClass == null) {
            throw new RuntimeException("cannot automatically resolve persistentClass, please set the property manually");
        }
    }

}
