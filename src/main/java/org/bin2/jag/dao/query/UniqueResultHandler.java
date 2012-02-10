package org.bin2.jag.dao.query;

import org.hibernate.Query;

/** 
 * return a unique result as a result of the query
 * @see Query#uniqueResult
 **/
public class UniqueResultHandler<T> implements ResultHandler<T> {

    /** 
     * @return a unique result as a result of the query
     * @see Query#uniqueResult
     **/
    public T result(Query query) {
        return (T) query.uniqueResult();
    }
}
