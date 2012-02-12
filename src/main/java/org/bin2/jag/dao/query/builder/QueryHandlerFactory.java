package org.bin2.jag.dao.query.builder;

import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.query.QueryHandler;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author broger
 *         Date: 12/02/12
 *         Time: 13:20
 */
public interface QueryHandlerFactory<T extends Annotation> {
    QueryHandler build(@Nullable T annotation, Class<? extends Dao> daoClass, Method method);
}
