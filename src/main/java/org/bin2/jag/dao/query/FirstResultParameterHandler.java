package org.bin2.jag.dao.query;

import org.hibernate.Query;

/**
 * set the firstResult of the query using the param value
 * @see Query#setFirstResult
 **/
public class FirstResultParameterHandler implements ParameterHandler {


    /**
     * set the firstResult of the query using the param value
     * @see Query#setFirstResult
     * @param param use this value as firstResult
     * @throws ClassCastException if param is not an instance of Number
     **/
    public void proceedParameter(Query query, Object param) {
        query.setFirstResult(((Number) param).intValue());
    }
}
