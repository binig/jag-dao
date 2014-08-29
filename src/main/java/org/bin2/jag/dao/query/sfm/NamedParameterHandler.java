package org.bin2.jag.dao.query.sfm;

import org.bin2.jag.dao.query.ParameterHandler;
import org.hibernate.Query;

import java.sql.PreparedStatement;

/**
 * set the param on the query using the name passed in ctor
 * @see org.hibernate.Query#setParameter(String , Object)
 **/
public class NamedParameterHandler implements ParameterHandler<JdbcQuery> {
    private final String name;

    /**
     * @param name the name of the parameter in the query
     **/
    public NamedParameterHandler(String name) {
        this.name = name;
    }

    /**
     * set the parameter on the query using the name passed in the ctor
     * @see org.hibernate.Query#setParameter(String , Object)
     **/
    public void proceedParameter(JdbcQuery query, Object param) {

        throw new UnsupportedOperationException();
    }
}
