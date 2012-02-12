package org.bin2.jag.dao.query.builder;

import com.google.common.base.Preconditions;
import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.Query;
import org.bin2.jag.dao.query.BasicQueryHandler;
import org.bin2.jag.dao.query.QueryHandler;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * Handle the {@see Query} annotation does not handle the no annotation case (NPE)
 * build a {@see BasicQueryHandler} with the {@see Query#value}  as query string
 *
 * @author broger
 */
public class QueryHandlerFactoryImpl implements QueryHandlerFactory<Query> {
    @Override
    /**
     * @return {@see BasicQueryHandler} with the {@see Query#value} as query string
     */
    public QueryHandler build(@Nullable Query annotation, Class<? extends Dao> daoClass, Method method) {
        Preconditions.checkNotNull(annotation);
        return new BasicQueryHandler(annotation.value());
    }
}
