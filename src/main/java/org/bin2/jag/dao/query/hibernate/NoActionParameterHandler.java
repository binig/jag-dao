package org.bin2.jag.dao.query.hibernate;

import org.bin2.jag.dao.query.ParameterHandler;
import org.hibernate.Query;

/**
 * This Parameter Handler just do nothing, it's just to avoid null value in the immutableList of handler
 * @see com.google.common.collect.ImmutableList
 **/
public enum NoActionParameterHandler implements ParameterHandler<Query> {
    INSTANCE;
    /**
     * do nothing
     **/
    public void proceedParameter(Query query, Object param) {
        //do Nothing
    }
}
