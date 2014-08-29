package org.bin2.jag.dao.query.hibernate.builder;

import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.FirstResult;
import org.bin2.jag.dao.query.builder.ParameterHandlerFactory;
import org.bin2.jag.dao.query.hibernate.FirstResultParameterHandler;
import org.bin2.jag.dao.query.ParameterHandler;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * Build a {@see FirstResultParameterHandler}
 *
 * @author broger
 */
public class FirstResultParameterHandlerFactory implements ParameterHandlerFactory<FirstResult> {

    @Override
    public ParameterHandler build(@Nullable FirstResult annotation, Class<? extends Dao> daoClass, Method method, int parameterIdx) {
        return new FirstResultParameterHandler();
    }
}
