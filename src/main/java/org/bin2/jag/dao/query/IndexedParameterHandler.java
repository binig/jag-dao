package org.bin2.jag.dao.query;

import org.hibernate.Query;


/**
 * set the param on the query using the index passed in ctor
 * @see Query#setParameter(int , Object)
 **/
public class IndexedParameterHandler implements ParameterHandler {
    private final int index;

    /**
     * @param index the index of the param on the query
     **/
    public IndexedParameterHandler(int index) {
        this.index = index;
    }

    /**
     * set the parameter on the query using the index passed in the ctor
     * @see Query#setParameter(int , Object)
     **/
    public void proceedParameter(Query query, Object param) {
        query.setParameter(index, param);
    }
}
