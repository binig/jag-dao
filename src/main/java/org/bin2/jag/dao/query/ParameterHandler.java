package org.bin2.jag.dao.query;

import org.hibernate.Query;
import org.hibernate.Session;

public interface ParameterHandler {
   
    void proceedParameter(Query query, Object param);
}
