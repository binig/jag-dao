package org.bin2.jag.dao.query.builder;

import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.FetchSize;
import org.bin2.jag.dao.query.FetchSizeParameterHandler;
import org.bin2.jag.dao.query.ParameterHandler;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * Build a {@see FetchSizeParameterHandler}
 *
 * @author broger
 */
public class FetchSizeParameterHandlerFactory implements ParameterHandlerFactory<FetchSize> {

    @Override
    public ParameterHandler build(@Nullable FetchSize annotation, Class<? extends Dao> daoClass, Method method, int parameterIdx) {
        return new FetchSizeParameterHandler();
    }
}
