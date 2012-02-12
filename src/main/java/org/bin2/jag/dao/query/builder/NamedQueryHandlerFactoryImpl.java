package org.bin2.jag.dao.query.builder;

import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.NamedQuery;
import org.bin2.jag.dao.query.NamedQueryHandler;
import org.bin2.jag.dao.query.QueryHandler;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * @author broger
 *         Date: 12/02/12
 *         Time: 13:24
 */
public class NamedQueryHandlerFactoryImpl implements QueryHandlerFactory<NamedQuery> {
    @Override
    public QueryHandler build(@Nullable NamedQuery annotation, Class<? extends Dao> daoClass, Method method) {
        final String queryName = annotation == null || annotation.value().length() == 0 ? buildName(daoClass, method) : annotation.value();
        return new NamedQueryHandler(queryName);
    }

    private String buildName(Class<? extends Dao> daoClass, Method method) {
        return new StringBuilder(daoClass
                .getSimpleName()).append(".").append(method.getName()).toString();
    }
}
