package org.bin2.jag.dao.query.hibernate;

import org.bin2.jag.dao.query.ParameterHandler;
import org.hibernate.Query;

/**
 * set the param on the query using the name passed in ctor
 * @see Query#setParameter(String , Object)
 **/
public class NamedParameterHandler implements ParameterHandler<Query> {
    private final String name;

    /**
     * @param name the name of the parameter in the query
     **/
    public NamedParameterHandler(String name) {
        this.name = name;
    }

    /**
     * set the parameter on the query using the name passed in the ctor
     * @see Query#setParameter(String , Object)
     **/
    public void proceedParameter(Query query, Object param) {
        query.setParameter(name, param);
    }
}
