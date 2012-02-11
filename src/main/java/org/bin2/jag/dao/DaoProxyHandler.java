package org.bin2.jag.dao;

import com.google.common.collect.ImmutableMap;
import org.bin2.jag.dao.query.ParameterHandler;
import org.bin2.jag.dao.query.QueryContext;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Invocation handler of the Dao interface
 * @see Dao
 * @see DaoBeanFactory 
 **/
public class DaoProxyHandler implements InvocationHandler {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DaoProxyHandler.class);
    private final SessionFactory sessionFactory;
    private final Class<?> daoClass;
    private final Class<?> persistentClass;
    private final Map<Method, QueryContext> queryContexts;

    /**
     * The invocation handler for the proxy of the dao interface
     * @see DaoBeanFactory
     * @see Dao
     * @param sessionFactory the sessionFactory the query will be done on
     * @param daoClass the daoClass interfaces to implement
     * @param persistentClass the persited class manage by the daoClass
     * @param queryContexts one context by method, the context define the handlers to manage the parameters,
     *               to build the query and the result object
     **/
    public DaoProxyHandler(final SessionFactory sessionFactory,
                           final Class<?> daoClass, final Class<?> persistentClass, Map<Method, QueryContext> queryContexts) {
        super();
        this.sessionFactory = sessionFactory;
        this.daoClass = daoClass;
        this.persistentClass = persistentClass;
        this.queryContexts = ImmutableMap.copyOf(queryContexts);
    }

    /**
     * @param o object to persist
     * @see org.hibernate.Session#save
     **/
    public void create(final Object o) {
        this.sessionFactory.getCurrentSession().save(o);

    }

    /**
     * @param o object to delete
     * @see org.hibernate.Session#delete
     **/
    public void delete(final Object o) {
        this.sessionFactory.getCurrentSession().delete(o);

    }


    /**
     * @param m method the dao interface
     * @param args parameters put through the method
     * @return the query result type (List/Iterator/Object) according to the method return type
     **/
    public Object executeNamedQuery(final Method m, final Object[] args) {
        QueryContext ctx = this.queryContexts.get(m);
        Query query = ctx.getQueryHandler().getQuery(this.sessionFactory.getCurrentSession());
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
            result = load((Serializable) args[0]);
        } else if ("delete".equals(m.getName())) {
            result = null;
            delete(args[0]);
        } else if ("create".equals(m.getName())) {
            result = null;
            create(args[0]);
        } else {
            // namedquery case;
            result = executeNamedQuery(m, args);
        }
        return result;
    }

 
    /**
     * @param id id of the object to load
     * @see org.hibernate.Session#load
     **/
    public Object load(final Serializable id) {
        return this.sessionFactory.getCurrentSession().load(
                this.persistentClass, id);
    }
}
