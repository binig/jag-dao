package org.bin2.jag.dao.query.sfm;

import com.google.common.base.Throwables;
import org.bin2.jag.dao.query.ParameterHandler;
import org.hibernate.Query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * use the param to set the fetchSize of the query
 */
public class FetchSizeParameterHandler implements ParameterHandler<JdbcQuery> {

    /**
     * set the fetchSize of the query using param value
     * @see Statement#setFetchSize
     * @param param the fetch size value should be a Number
     * @throws ClassCastException if param is not an instance of Number
     **/ 
    public void proceedParameter(JdbcQuery query, Object param) {
        try {
            query.getStatement().setFetchSize(((Number) param).intValue());
        } catch (SQLException e) {
            throw Throwables.propagate(e);
        }
    }
}
