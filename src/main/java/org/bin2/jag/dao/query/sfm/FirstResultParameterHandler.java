package org.bin2.jag.dao.query.sfm;

import org.bin2.jag.dao.query.ParameterHandler;
import org.hibernate.Query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * set the firstResult of the query using the param value
 * @see org.hibernate.Query#setFirstResult
 **/
public class FirstResultParameterHandler implements ParameterHandler<JdbcQuery> {

    private static class FirstResultPrefetchAction implements PrefetchAction {
        private final int firstResult;

        private FirstResultPrefetchAction(int firstResult) {
            this.firstResult = firstResult;
        }

        @Override
        public void execute(ResultSet rs) throws SQLException{
            rs.absolute(firstResult);
        }
    }
    /**
     * set the firstResult of the query using the param value
     * @see org.hibernate.Query#setFirstResult
     * @param param use this value as firstResult
     * @throws ClassCastException if param is not an instance of Number
     **/
    public void proceedParameter(JdbcQuery query, Object param) {
         query.addPrefetchAction( new FirstResultPrefetchAction(((Number)param).intValue()));
    }
}
