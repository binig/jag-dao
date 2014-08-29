package org.bin2.jag.dao.query.sfm.builder;

import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.FetchSize;
import org.bin2.jag.dao.query.ParameterHandler;
import org.bin2.jag.dao.query.builder.ParameterHandlerFactory;
import org.bin2.jag.dao.query.sfm.MaxResultParameterHandler;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * Build a {@see FetchSizeParameterHandler}
 * 
 * @author broger
 */
public class MaxResultParameterHandlerFactory implements ParameterHandlerFactory<FetchSize> {

	@Override
	public ParameterHandler build(@Nullable final FetchSize annotation, final Class<? extends Dao> daoClass, final Method method, final int parameterIdx) {
		return new MaxResultParameterHandler();
	}
}
