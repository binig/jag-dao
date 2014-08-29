package org.bin2.jag.dao.query.sfm;

import org.bin2.jag.dao.query.ParameterHandler;
import org.hibernate.Query;

import java.sql.PreparedStatement;

/**
 * This Parameter Handler just do nothing, it's just to avoid null value in the immutableList of handler
 * @see com.google.common.collect.ImmutableList
 **/
public enum NoActionParameterHandler implements ParameterHandler<JdbcQuery> {
    INSTANCE;
    /**
     * do nothing
     **/
    public void proceedParameter(JdbcQuery query, Object param) {
        //do Nothing
    }
}
