package org.bin2.jag.dao.query.builder;

import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.query.QueryHandler;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Manage the creation of the QueryHandler for a specific Annotation
 *
 * @param <T> the type of the annotation this factory manage
 * @see org.bin2.jag.dao.query.hibernate.builder.HibernateQueryContextBuilder
 * @see QueryContextBuilder
 */
public interface QueryHandlerFactory<T extends Annotation> {
    /**
     * @param annotation the method annotation
     * @param daoClass   the dao class
     * @param method     the method of the dao class
     * @return the queryHandler that will manage the creation of the query for the method of the dao daoClass
     */
    QueryHandler build(@Nullable T annotation, Class<? extends Dao> daoClass, Method method);
}
