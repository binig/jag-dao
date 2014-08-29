package org.bin2.jag.dao.query.sfm;

import org.bin2.jag.dao.query.QueryHandler;
import org.hibernate.Query;
import org.hibernate.Session;

import java.sql.Connection;

/**
 * build a namedQuery using the ctor arg as namedQuery name
 * @see org.hibernate.Session#getNamedQuery(String )
 **/
public class NamedQueryHandler implements QueryHandler<JdbcQuery,Connection> {
    private final String query;

    /**
     * @param query the name of the namedQuery
     **/
    public NamedQueryHandler(String query) {
        this.query = query;
    }

    /**
     * build a namedQuery using the ctor arg namedQuery name
     * @see org.hibernate.Session#getNamedQuery(String )
     * @return the hql query
     **/
    public JdbcQuery getQuery(Connection session) {
        throw new UnsupportedOperationException();
    }
}
