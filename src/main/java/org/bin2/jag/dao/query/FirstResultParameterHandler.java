package org.bin2.jag.dao.query;

import org.hibernate.Query;
import org.hibernate.Session;

public class FirstResultParameterHandler implements ParameterHandler {

    public void proceedParameter(Query query, Object param) {
        query.setFirstResult(((Number) param).intValue());
    }
}
