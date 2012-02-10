package org.bin2.jag.dao.query;

import org.hibernate.Query;

/**
 * This Parameter Handler just do nothing, it's just to avoid null value in the immutableList of handler
 * @see com.google.common.collect.ImmutableList
 **/
public enum NoActionParameterHandler implements ParameterHandler {
    INSTANCE;
    /**
     * do nothing
     **/
    public void proceedParameter(Query query, Object param) {
        //do Nothing
    }
}
