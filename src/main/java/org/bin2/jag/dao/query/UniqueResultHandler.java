package org.bin2.jag.dao.query;

import org.hibernate.Query;

public class UniqueResultHandler<T> implements ResultHandler<T> {

    public T result(Query query) {
        return (T) query.uniqueResult();
    }
}
