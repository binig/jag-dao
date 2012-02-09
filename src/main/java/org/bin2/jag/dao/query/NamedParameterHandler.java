package org.bin2.jag.dao.query;

import org.hibernate.Query;

public class NamedParameterHandler implements ParameterHandler {
    private final String name;

    public NamedParameterHandler(String name) {
        this.name = name;
    }

    public void proceedParameter(Query query, Object param) {
        query.setParameter(name, param);
    }
}
