package org.bin2.jag.dao.query.hibernate.builder;

import com.google.common.base.Preconditions;
import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.NamedParameter;
import org.bin2.jag.dao.query.builder.ParameterHandlerFactory;
import org.bin2.jag.dao.query.hibernate.NamedParameterHandler;
import org.bin2.jag.dao.query.ParameterHandler;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * Build a {@see NamedParameterHandler} with the {@see NamedParameter#value}
 * as query parameter name
 *
 * @author broger
 */
public class NamedParameterHandlerFactory implements ParameterHandlerFactory<NamedParameter> {

    @Override
    public ParameterHandler build(@Nullable NamedParameter annotation, Class<? extends Dao> daoClass, Method method, int parameterIdx) {
        Preconditions.checkNotNull(annotation);
        return new NamedParameterHandler(annotation.value());
    }
}
