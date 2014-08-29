package org.bin2.jag.dao.query.sfm;

import com.google.common.base.Throwables;
import org.bin2.jag.dao.query.ParameterHandler;
import org.hibernate.Query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * set the firstResult of the query using the param value
 * 
 * @see org.hibernate.Query#setFirstResult
 **/
public class MaxResultParameterHandler implements ParameterHandler<JdbcQuery> {

	/**
	 * set the firstResult of the query using the param value
	 *
	 * @see org.hibernate.Query#setMaxResults(int)
	 * @param param use this value as firstResult
	 * @throws ClassCastException if param is not an instance of Number
	 **/
	@Override
	public void proceedParameter(final JdbcQuery query, final Object param) {
        try {
            query.getStatement().setMaxRows(((Number) param).intValue());
        } catch (SQLException e) {
            throw Throwables.propagate(e);
        }
    }
}
