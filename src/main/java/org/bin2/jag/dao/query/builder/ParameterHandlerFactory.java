package org.bin2.jag.dao.query.builder;

import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.query.ParameterHandler;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * Manage the creation of the Parameter for a specific Annotation
 *
 * @param <T> the type of the annotation this factory manage
 * @author broger
 * @see org.bin2.jag.dao.query.hibernate.builder.HibernateQueryContextBuilder
 * @see QueryContextBuilder
 */
public interface ParameterHandlerFactory<T> {
    /**
     * @param annotation   the paramter annotation
     * @param daoClass     the dao class
     * @param method       the method of the dao class
     * @param parameterIdx the index of the parameter in the method
     * @return the {@see ParameterHandler} that will manage the paramter for the query for the method of the dao daoClass
     */
    ParameterHandler build(@Nullable T annotation, Class<? extends Dao> daoClass, Method method, int parameterIdx);
}
