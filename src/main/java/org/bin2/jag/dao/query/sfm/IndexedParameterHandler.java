package org.bin2.jag.dao.query.sfm;

import com.google.common.base.Throwables;
import org.bin2.jag.dao.query.ParameterHandler;
import org.hibernate.Query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * set the param on the query using the index passed in ctor
 * @see org.hibernate.Query#setParameter(int , Object)
 **/
public class IndexedParameterHandler implements ParameterHandler<JdbcQuery> {
    private final int index;

    /**
     * @param index the index of the param on the query
     **/
    public IndexedParameterHandler(int index) {
        this.index = index;
    }

    /**
     * set the parameter on the query using the index passed in the ctor
     * @see org.hibernate.Query#setParameter(int , Object)
     **/
    public void proceedParameter(JdbcQuery query, Object param) {

        try {
            query.getStatement().setObject(index, param);
        } catch (SQLException e) {
            throw Throwables.propagate(e);
        }
    }
}
