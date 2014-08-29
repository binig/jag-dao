package org.bin2.jag.dao.query.sfm;

import org.bin2.jag.dao.query.ResultHandler;
import org.hibernate.Query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;


/** 
 * return an iterator as a result of the query
 * @see org.hibernate.Query#iterate
 **/
public class IteratorResultHandler<T> implements ResultHandler<JdbcQuery<T>,Iterator<T>> {

    /**
     * @return an iterator as a result of the query
     * @see org.hibernate.Query#iterate
     **/
    public Iterator<T> result(JdbcQuery<T> query) {
        ResultSet rs = query.performQuery();
        return new JdbcIterator<T>(rs,query.getMapper());
    }
}
