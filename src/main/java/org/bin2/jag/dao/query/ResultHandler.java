package org.bin2.jag.dao.query;

import org.hibernate.Query;

/**
 * used by the DaoProxyHandler to build the result from query<br>
 * one by method / dao<br>
 * the implementation <b>should be thread safe</b>
 * @see org.bin2.jag.dao.Dao
 * @see org.bin2.jag.dao.DaoProxyHandler
 * @see org.bin2.jag.dao.DaoBeanFactory
 **/
public interface ResultHandler<T> {

    /**
     * compute the result of the query
     * @param query the query to compute
     * @return the computed result
     **/
    T result(Query query);
}
