package org.bin2.jag.dao.query.sfm.builder;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.Query;
import org.bin2.jag.dao.query.QueryHandler;
import org.bin2.jag.dao.query.builder.QueryHandlerFactory;
import org.bin2.jag.dao.query.sfm.BasicQueryHandler;
import org.sfm.jdbc.JdbcMapperFactory;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.List;

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
        return new BasicQueryHandler(annotation.value(), JdbcMapperFactory.newInstance().newMapper(getTargetClass(method)));
    }

    private Class<?> getTargetClass(Method m) {
        Class<?> returnClass = m.getReturnType();
        if (List.class.isAssignableFrom(returnClass)|| Iterator.class.isAssignableFrom(returnClass)) {
            return (Class<?>)((ParameterizedType)m.getGenericReturnType()).getActualTypeArguments()[0];
        } else {
            return returnClass;
        }
    }
}
