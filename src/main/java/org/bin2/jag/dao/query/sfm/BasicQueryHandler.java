package org.bin2.jag.dao.query.sfm;

import com.google.common.base.Throwables;
import org.bin2.jag.dao.query.QueryHandler;
import org.sfm.jdbc.JdbcMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * build a query using the ctor arg query
 * @see org.hibernate.Session#createQuery(String )
 **/
public class BasicQueryHandler<R> implements QueryHandler<JdbcQuery,Connection> {
    private final String query;
    private final JdbcMapper<R> mapper;

    /**
     * @param query the hql query string
     **/
    public BasicQueryHandler(String query, JdbcMapper<R> mapper) {
        this.query = query;
        this.mapper = mapper;
    }

    /**
     * build a query using the ctor arg query
     * @see org.hibernate.Session#createQuery(String )
     * @return the hql query
     **/
    public JdbcQuery getQuery(Connection session) {
        return new JdbcQuery(query, session, mapper);
    }
}
