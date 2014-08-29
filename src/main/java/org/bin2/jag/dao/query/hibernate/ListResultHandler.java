package org.bin2.jag.dao.query.hibernate;

import org.bin2.jag.dao.query.ResultHandler;
import org.hibernate.Query;

import java.util.List;

/** 
 * return a list as a result of the query
 * @see Query#list
 **/
public class ListResultHandler<T> implements ResultHandler<Query,List<T>> {

    /** 
     * @return a list as a result of the query
     * @see Query#list
     **/
    public List<T> result(Query query) {
        return query.list();
    }
}
