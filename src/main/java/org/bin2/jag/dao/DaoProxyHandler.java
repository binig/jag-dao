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


public class DaoProxyHandler implements InvocationHandler {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DaoProxyHandler.class);
    private final SessionFactory sessionFactory;
    private final Class<?> daoClass;
    private final Class<?> persistentClass;
    private final Map<Method, QueryContext> queryContexts;

    public DaoProxyHandler(final SessionFactory sessionFactory,
                           final Class<?> daoClass, final Class<?> persistentClass, Map<Method, QueryContext> queryContexts) {
        super();
        this.sessionFactory = sessionFactory;
        this.daoClass = daoClass;
        this.persistentClass = persistentClass;
        this.queryContexts = ImmutableMap.copyOf(queryContexts);
    }


    public void create(final Object o) {
        this.sessionFactory.getCurrentSession().save(o);

    }

    public void delete(final Object o) {
        this.sessionFactory.getCurrentSession().delete(o);

    }


    public Object executeNamedQuery(final Method m, final Object[] args) {
        QueryContext ctx = this.queryContexts.get(m);
        Query query = ctx.getQueryHandler().getQuery(this.sessionFactory.getCurrentSession());
        for (int i = 0; args != null && i < args.length; i++) {
            ParameterHandler ph = ctx.getParameterHandlers().get(i);
            if (ph != null) {
                ph.proceedParameter(query, args[i]);
            }
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

    public Object load(final Serializable id) {
        return this.sessionFactory.getCurrentSession().load(
                this.persistentClass, id);
    }
}
