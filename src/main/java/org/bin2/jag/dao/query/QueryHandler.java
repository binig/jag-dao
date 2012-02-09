package org.bin2.jag.dao.query;

import org.hibernate.Query;
import org.hibernate.Session;

public interface QueryHandler {
   
	Query getQuery(Session session);
}
