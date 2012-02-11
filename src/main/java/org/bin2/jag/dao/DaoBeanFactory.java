package org.bin2.jag.dao;

import com.google.common.base.Preconditions;
import org.bin2.jag.dao.query.builder.QueryContextBuilder;
import org.bin2.jag.dao.query.builder.StandardQueryContextBuilder;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

/**
 * Spring FactoryBean used to build the Dao for the interface specified
 * <ul>
 * <li> sessionFactory property is mandatory
 * <li> dao class property is mandatory
 * <li> persitentClass is optional, if none is provided :
 * the factoryBean try to
 * compute it automatically by getting the type specified on the Dao (User for
 * the previous example). If no persistent class is set and it cannot
 * automatically fetch the type the factory will throw an exception.
 * <li>the factory used a queryContextBuilder to extra query build inforation from the daoClass, if no queryContextBuilder is specfied
 * it used the StandardQueryContextBuilder</li>
 * </ul>
 *
 * @see StandardQueryContextBuilder
 * @see QueryContextBuilder
 */
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
     * query context builder
     */
    private QueryContextBuilder queryContextBuilder;

    /**
     * @param queryContextBuilder the query context builder that extract query context from the dao class
     */
    public DaoBeanFactory(QueryContextBuilder queryContextBuilder) {
        this.queryContextBuilder = queryContextBuilder;
    }

    /**
     * build a DaoBeanFactory with the default queryContextBuilder (  StandardQueryContextBuilder )
     *
     * @see QueryContextBuilder
     * @see StandardQueryContextBuilder
     */
    public DaoBeanFactory() {
        this(new StandardQueryContextBuilder());
    }

    /**
     * @return the dao class to implement
     */
    public Class<? extends Dao<?, ? extends Serializable>> getDao() {
        return this.dao;
    }

    @Override
    public Dao<?, ?> getObject() throws Exception {
        if (this.persistentClass == null) {
            tryingExtractPersistentType();
        }
        final DaoProxyHandler handler = new DaoProxyHandler(
                this.sessionFactory, this.dao, this.persistentClass, queryContextBuilder.buildQueryContexts(dao));
        return Dao.class
                .cast(Proxy.newProxyInstance(Thread.currentThread()
                        .getContextClassLoader(), new Class<?>[]{this.dao},
                        handler));
    }


    @Override
    public Class<? extends Dao<?, ? extends Serializable>> getObjectType() {
        return this.dao;
    }

    /**
     * @return the sessionFactory used for the created Dao
     */
    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    @Override
    public boolean isSingleton() {
        return this.singleton;
    }

    /**
     * @param dao the dao class to implement
     */
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
     *
     * @param persistentClass the persistent clas managed by the created Dao
     */
    public void setPersistent(final Class<?> persistentClass) {
        this.persistentClass = persistentClass;
    }

    /**
     * @param sessionFactory the sessionFactory used by the dao
     */
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @param singleton true to have a singleton @see #isSingleton()
     */
    public void setSingleton(final boolean singleton) {
        this.singleton = singleton;
    }

    private void tryingExtractPersistentType() {
        Preconditions.checkArgument(Dao.class.isAssignableFrom(dao), "cannot proxy a non Dao interface " + dao.getName());
        // try to detect the type of the dao
        for (final Type type : dao.getGenericInterfaces()) {
            if (type instanceof ParameterizedType) {
                final ParameterizedType p = (ParameterizedType) type;
                final Class<?> rtype = (Class<?>) p.getRawType();
                if (Dao.class.isAssignableFrom(rtype)) {
                    Type t = p.getActualTypeArguments()[0];
                    if (t instanceof Class) {
                        this.persistentClass = (Class<?>) t;
                    } else {
                        throw new IllegalArgumentException("cannot automatically resolve persistentClass, please set the property manually, type not define " + t);
                    }
                    break;
                }
            }
        }
    }
}
