package org.bin2.jag.dao.query.builder;

import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.query.ResultHandler;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * Manage the creation of the ResultHandler  for a specific Method/Annotation
 *
 * @param <T> the type of the annotation this factory manage
 * @author broger
 * @see StandardQueryContextBuilder
 * @see QueryContextBuilder
 */
public interface ResultHandlerFactory<T> {
    /**
     * @param annotation   the method annotation
     * @param daoClass     the dao class
     * @param method       the method of the dao class
     * @return the {@see ResultHandler} that will handle the query for the method of the dao daoClass to produce the result
     */
    ResultHandler build(@Nullable T annotation, Class<? extends Dao> daoClass, Method method);
}
