package org.bin2.jag.dao.query.sfm.builder;

import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.NamedQuery;
import org.bin2.jag.dao.query.QueryHandler;
import org.bin2.jag.dao.query.builder.QueryHandlerFactory;
import org.bin2.jag.dao.query.sfm.NamedQueryHandler;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * Handle the building of the QueryHandler for the NamedQuery annotation
 * and for no annotation, if no annotation use the simpleName + "." + methodName
 * as nameQuery name.
 *
 * @author broger
 * @see Class#getSimpleName()
 * @see java.lang.reflect.Method#getName()
 * @see org.bin2.jag.dao.NamedQuery
 * @see org.bin2.jag.dao.query.hibernate.NamedQueryHandler
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
