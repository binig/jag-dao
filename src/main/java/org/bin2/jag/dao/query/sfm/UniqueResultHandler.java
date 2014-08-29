package org.bin2.jag.dao.query.sfm;

import com.google.common.base.Throwables;
import org.bin2.jag.dao.query.ResultHandler;
import org.hibernate.Query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/** 
 * return a unique result as a result of the query
 * @see org.hibernate.Query#uniqueResult
 **/
public class UniqueResultHandler<T> implements ResultHandler<JdbcQuery<T>,T> {

    /**
     * @return a unique result as a result of the query
     * @see org.hibernate.Query#uniqueResult
     **/
    public T result(JdbcQuery<T> query) {

        ResultSet rs = query.performQuery();
        try {
            if (rs.next()) {
                return query.getMapper().map(rs);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
