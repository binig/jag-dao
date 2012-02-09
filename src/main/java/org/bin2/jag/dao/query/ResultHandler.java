package org.bin2.jag.dao.query;

import org.hibernate.Query;

public interface  ResultHandler<T> {
   
	T result(Query query);
}
