package org.bin2.jag.dao.query;

import org.hibernate.Query;

import java.util.Iterator;


/** 
 * return an iterator as a result of the query
 * @see Query#iterate
 **/
public class IteratorResultHandler<T> implements ResultHandler<Iterator<T>> {

    /** 
     * @return an iterator as a result of the query
     * @see Query#iterate
     **/
    public Iterator<T> result(Query query) {
        return query.iterate();
    }
}
