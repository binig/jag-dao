package org.bin2.jag.dao.query;

import org.hibernate.Query;

/**
 * use the param to set the fetchSize of the query
 */
public class FetchSizeParameterHandler implements ParameterHandler {

    /**
     * set the fetchSize of the query using param value
     * @see Query#setFetchSize
     * @param param the fetch size value should be a Number
     * @throws ClassCastException if param is not an instance of Number
     **/ 
    public void proceedParameter(Query query, Object param) {
        query.setFetchSize(((Number) param).intValue());
    }
}
