package org.bin2.jag.dao.query.hibernate;

import org.bin2.jag.dao.query.QueryHandler;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * build a namedQuery using the ctor arg as namedQuery name
 * @see Session#getNamedQuery(String )
 **/
public class NamedQueryHandler implements QueryHandler<Query,Session> {
    private final String query;

    /**
     * @param query the name of the namedQuery
     **/
    public NamedQueryHandler(String query) {
        this.query = query;
    }

    /**
     * build a namedQuery using the ctor arg namedQuery name
     * @see Session#getNamedQuery(String )
     * @return the hql query
     **/
    public Query getQuery(Session session) {
        return session.getNamedQuery(query);
    }
}
