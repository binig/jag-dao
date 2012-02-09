package org.bin2.jag.dao.query;

import org.hibernate.Query;

import java.util.Iterator;

public class IteratorResultHandler<T> implements ResultHandler<Iterator<T>> {

    public Iterator<T> result(Query query) {
        return query.iterate();
    }
}
