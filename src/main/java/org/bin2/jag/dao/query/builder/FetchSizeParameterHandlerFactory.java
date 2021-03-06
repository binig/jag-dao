package org.bin2.jag.dao.query.builder;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.FetchSize;
import org.bin2.jag.dao.query.FetchSizeParameterHandler;
import org.bin2.jag.dao.query.ParameterHandler;

/**
 * Build a {@see MaxResultParameterHandler}
 * 
 * @author broger
 */
public class FetchSizeParameterHandlerFactory implements ParameterHandlerFactory<FetchSize> {

	@Override
	public ParameterHandler build(@Nullable final FetchSize annotation, final Class<? extends Dao> daoClass, final Method method, final int parameterIdx) {
		return new FetchSizeParameterHandler();
	}
}
