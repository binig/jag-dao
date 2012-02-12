package org.bin2.jag.dao.query.builder;

import com.google.common.base.Preconditions;
import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.Query;
import org.bin2.jag.dao.query.BasicQueryHandler;
import org.bin2.jag.dao.query.QueryHandler;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * @author broger
 *         Date: 12/02/12
 *         Time: 13:30
 */
public class QueryHandlerFactoryImpl implements QueryHandlerFactory<Query> {
    @Override
    public QueryHandler build(@Nullable Query annotation, Class<? extends Dao> daoClass, Method method) {
        Preconditions.checkNotNull(annotation);
        Preconditions.checkNotNull(annotation.value());
        return new BasicQueryHandler(annotation.value());
    }
}
