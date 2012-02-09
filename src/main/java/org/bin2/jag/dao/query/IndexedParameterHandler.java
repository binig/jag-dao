package org.bin2.jag.dao.query;

import org.hibernate.Query;

public class IndexedParameterHandler implements ParameterHandler {
    private final int index;

    public IndexedParameterHandler(int index) {
        this.index = index;
    }

    public void proceedParameter(Query query, Object param) {
        query.setParameter(index, param);
    }
}
