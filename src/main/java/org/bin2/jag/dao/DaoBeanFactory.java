package org.bin2.jag.dao;

import com.google.common.base.Preconditions;
import org.bin2.jag.dao.query.builder.QueryContextBuilder;
import org.bin2.jag.dao.query.hibernate.builder.HibernateQueryContextBuilder;
import org.hibernate.Session;
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
 * @see org.bin2.jag.dao.query.hibernate.builder.HibernateQueryContextBuilder
 * @see QueryContextBuilder
 */
public class DaoBeanFactory<Q,S,T,PK extends Serializable> implements FactoryBean<Dao<T, PK>> {
    private boolean singleton;
    /**
     * the dao class to implement
     */
    private Class<? extends Dao<T, PK>> dao;

    /**
     * optional persistentClass
     * if not provided will check the parametized type of the DAO
     */
    private Class<T> persistentClass;


    /**
     * query context builder
     */
    private QueryContextBuilder<Q,S,T> queryContextBuilder;

    private InnerBaseDao<T,PK,S> innerBaseDao;

    /**
     * @param queryContextBuilder the query context builder that extract query context from the dao class
     */
    public DaoBeanFactory(QueryContextBuilder<Q,S,T> queryContextBuilder) {
        this.queryContextBuilder = queryContextBuilder;
    }

    /**
     * @return the dao class to implement
     */
    public Class<? extends Dao<?, ? extends Serializable>> getDao() {
        return this.dao;
    }

    @Override
    public Dao<T, PK> getObject() throws Exception {
        if (this.persistentClass == null) {
            tryingExtractPersistentType();
        }
        innerBaseDao.setPersistentClass(persistentClass);
        final DaoProxyHandler<Q,S,T,PK> handler = new DaoProxyHandler<Q,S,T,PK>(
                this.innerBaseDao, this.dao, this.persistentClass, queryContextBuilder.buildQueryContexts(persistentClass,dao));
        return Dao.class
                .cast(Proxy.newProxyInstance(Thread.currentThread()
                        .getContextClassLoader(), new Class<?>[]{this.dao},
                        handler));
    }


    @Override
    public Class<? extends Dao<?, ? extends Serializable>> getObjectType() {
        return this.dao;
    }


    @Override
    public boolean isSingleton() {
        return this.singleton;
    }

    /**
     * @param dao the dao class to implement
     */
    public void setDao(final Class<? extends Dao<T, PK>> dao) {
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
    public void setPersistent(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public void setInnerBaseDao(InnerBaseDao<T, PK, S> innerBaseDao) {
        this.innerBaseDao = innerBaseDao;
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
                        this.persistentClass = (Class<T>) t;
                    } else {
                        throw new IllegalArgumentException("cannot automatically resolve persistentClass, please set the property manually, type not define " + t);
                    }
                    break;
                }
            }
        }
    }
}
