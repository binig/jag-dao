package org.bin2.jag.dao.query;

import org.hibernate.Query;

/**
 * used by the DaoProxyHandler to handle the method parameter<br>
 * one by method parameter / dao<br>
 * the implementation <b>should be thread safe</b>
 * @see org.bin2.jag.dao.Dao
 * @see org.bin2.jag.dao.DaoProxyHandler
 * @see org.bin2.jag.dao.DaoBeanFactory
 **/
public interface ParameterHandler<Q> {

    /**
     * proceed the method parameter
     * @param query the query to impact
     * @param param the method parameter
     **/
    void proceedParameter(Q query, Object param);
}
