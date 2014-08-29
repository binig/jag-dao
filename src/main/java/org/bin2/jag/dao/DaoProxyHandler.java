package org.bin2.jag.dao;

import com.google.common.collect.ImmutableMap;
import org.bin2.jag.dao.query.QueryContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Invocation handler of the Dao interface
 *
 * @see Dao
 * @see DaoBeanFactory
 */
public class DaoProxyHandler<Q,S,T,PK> implements InvocationHandler {

    //private final SessionFactory sessionFactory;
    private final InnerBaseDao<T,PK,S> innerBaseDao;
    private final Class<?> daoClass;
    private final Class<T> persistentClass;
    private final Map<Method, QueryContext<Q,S>> queryContexts;

    /**
     * The invocation handler for the proxy of the dao interface
     *
     * @param daoClass        the daoClass interfaces to implement
     * @param persistentClass the persited class manage by the daoClass
     * @param queryContexts   one context by method, the context define the handlers to manage the parameters,
     *                        to build the query and the result object
     * @see DaoBeanFactory
     * @see Dao
     */
    public DaoProxyHandler(final InnerBaseDao<T,PK,S> innerBaseDao,
                           final Class<?> daoClass, final Class<T> persistentClass, Map<Method, QueryContext<Q,S>> queryContexts) {
        super();
        this.innerBaseDao = innerBaseDao;
        this.daoClass = daoClass;
        this.persistentClass = persistentClass;
        this.queryContexts = ImmutableMap.copyOf(queryContexts);
    }

    /**
     * @param o object to persist
     * @see org.hibernate.Session#save
     */
    public void create(final T o) {
        innerBaseDao.create(o);

    }

    /**
     * @param o object to delete
     * @see org.hibernate.Session#delete
     */
    public void delete(final T o) {
        innerBaseDao.delete(o);

    }


    /**
     * @param m    method the dao interface
     * @param args parameters put through the method
     * @return the query result type (List/Iterator/Object) according to the method return type
     */
    public Object executeNamedQuery(final Method m, final Object[] args) {
        QueryContext<Q,S> ctx = this.queryContexts.get(m);
        Q query = ctx.getQueryHandler().getQuery(innerBaseDao.getSession());
        for (int i = 0; args != null && i < args.length; i++) {
            ctx.getParameterHandlers().get(i).proceedParameter(query, args[i]);
        }
        return ctx.getResultHandler().result(query);
    }


    @Override
    public Object invoke(final Object o, final Method m, final Object[] args)
            throws Throwable {
        final Object result;
        if ("toString".equals(m.getName())) {
            return this.daoClass.getName();
        } else if ("load".equals(m.getName())) {
            result = load((PK) args[0]);
        } else if ("delete".equals(m.getName())) {
            result = null;
            delete((T)args[0]);
        } else if ("create".equals(m.getName())) {
            result = null;
            create((T)args[0]);
        } else {
            // namedquery case;
            result = executeNamedQuery(m, args);
        }
        return result;
    }


    /**
     * @param id id of the object to load
     * @return the loaded object
     * @see org.hibernate.Session#load
     */
    public Object load(final PK id) {
        return this.innerBaseDao.load(id);
    }
}
