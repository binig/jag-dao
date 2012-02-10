package org.bin2.jag.dao.query;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * build a namedQuery using the ctor arg as namedQuery name
 * @see Session#getNamedQuery(String )
 **/
public class NamedQueryHandler implements QueryHandler {
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
