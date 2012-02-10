package org.bin2.jag.dao.query;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * used by the DaoProxyHandler to build the query <br>
 * one by method / dao <br>
 * the implementation <b>should be thread safe</b>
 * @see org.bin2.jag.dao.Dao
 * @see org.bin2.jag.dao.DaoProxyHandler
 * @see org.bin2.jag.dao.DaoBeanFactory
 **/
public interface QueryHandler {
    /**
     * build the query using the session session
     * @param session the session to be used
     * @return the builded query
     **/
    Query getQuery(Session session);
}
