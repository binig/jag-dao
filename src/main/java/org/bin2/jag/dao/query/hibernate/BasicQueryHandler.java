package org.bin2.jag.dao.query.hibernate;

import org.bin2.jag.dao.query.QueryHandler;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * build a query using the ctor arg query
 * @see Session#createQuery(String )
 **/
public class BasicQueryHandler implements QueryHandler<Query,Session> {
    private final String query;

    /**
     * @param query the hql query string
     **/
    public BasicQueryHandler(String query) {
        this.query = query;
    }

    /**
     * build a query using the ctor arg query
     * @see Session#createQuery(String )
     * @return the hql query
     **/
    public Query getQuery(Session session) {
        return session.createQuery(query);
    }
}
