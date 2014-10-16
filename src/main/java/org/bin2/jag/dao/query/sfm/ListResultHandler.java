package org.bin2.jag.dao.query.sfm;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.bin2.jag.dao.query.ResultHandler;
import org.sfm.utils.RowHandler;

import java.sql.ResultSet;
import java.util.List;

/** 
 * return a list as a result of the query
 * @see org.hibernate.Query#list
 **/
public class ListResultHandler<T> implements ResultHandler<JdbcQuery<T>,List<T>> {
    /**
     * @return a list as a result of the query
     * @see org.hibernate.Query#list
     **/
    public List<T> result(JdbcQuery<T> query) {
        ResultSet rs = query.performQuery();
        final List<T> result = Lists.newArrayList();
        try {
            query.getMapper().forEach(rs, new RowHandler<T>() {
                @Override
                public void handle(T t) throws Exception {
                    result.add(t);
                }
            });
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
        return result;
    }
}
