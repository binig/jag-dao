package org.bin2.jag.dao.query;

import org.hibernate.Query;
import java.util.List;

public class ListResultHandler<T> implements ResultHandler<List<T>> {
   
	public List<T> result(Query query) {
            return query.list();
        }
}
