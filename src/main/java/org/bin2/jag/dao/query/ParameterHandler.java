package org.bin2.jag.dao.query;

import org.hibernate.Query;

public interface ParameterHandler {

    void proceedParameter(Query query, Object param);
}
