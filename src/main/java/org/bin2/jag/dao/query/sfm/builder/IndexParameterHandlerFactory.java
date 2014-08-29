package org.bin2.jag.dao.query.sfm.builder;

import com.google.common.base.Preconditions;
import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.IndexParameter;
import org.bin2.jag.dao.NamedParameter;
import org.bin2.jag.dao.query.ParameterHandler;
import org.bin2.jag.dao.query.builder.ParameterHandlerFactory;
import org.bin2.jag.dao.query.sfm.IndexedParameterHandler;
import org.bin2.jag.dao.query.sfm.NamedParameterHandler;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * Build a {@see NamedParameterHandler} with the {@see NamedParameter#value}
 * as query parameter name
 *
 * @author broger
 */
public class IndexParameterHandlerFactory implements ParameterHandlerFactory<IndexParameter> {

    @Override
    public IndexedParameterHandler build(@Nullable IndexParameter annotation, Class<? extends Dao> daoClass, Method method, int parameterIdx) {
        Preconditions.checkNotNull(annotation);
        return new IndexedParameterHandler(annotation.value());
    }
}
