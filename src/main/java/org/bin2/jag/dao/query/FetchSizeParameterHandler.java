package org.bin2.jag.dao.query;

import org.hibernate.Query;
import org.hibernate.Session;

public class FetchSizeParameterHandler implements ParameterHandler {
    
    public void proceedParameter(Query query, Object param) {
        query.setFetchSize(((Number) param).intValue());
    }
}
