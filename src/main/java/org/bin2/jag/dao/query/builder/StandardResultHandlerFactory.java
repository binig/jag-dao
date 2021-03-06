package org.bin2.jag.dao.query.builder;

import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.query.*;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Manage the creation of the ResultHandler  for a specific Method/Annotation
 * Build  {@see ListQueryHandler} / {@see IteratorQueryHandler} / {@see UniqueQueryHandler} according to return type of the method
 * @param <T> the type of the annotation this factory manage
 * @author broger
 * @see StandardQueryContextBuilder
 * @see QueryContextBuilder
 */
public class StandardResultHandlerFactory<T> implements ResultHandlerFactory<T> {
    /**
     * compute the return type of the method :
     *  <ul>
     *  <li>for a {@see List} it build a {@see ListQueryHandler} </li>
     *  <li>for a {@see Iterator} it build a {@see IteratorQueryHandler} </li>
     *  <li>else  it build a {@see UniqueQueryHandler} </li>
     *  </ul>
     */
    @Override
    public ResultHandler build(@Nullable T annotation, Class<? extends Dao> daoClass, Method m) {
        final ResultHandler result;
        if (m.getReturnType().isAssignableFrom(List.class)) {
            result = new ListResultHandler();
        } else if (m.getReturnType().isAssignableFrom(Iterator.class)) {
            result = new IteratorResultHandler();
        } else {
            result = new UniqueResultHandler();
        }
        return result;
    }
}
