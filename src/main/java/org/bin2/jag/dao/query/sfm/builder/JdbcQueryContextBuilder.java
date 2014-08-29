package org.bin2.jag.dao.query.sfm.builder;

import com.google.common.collect.ImmutableMap;
import org.bin2.jag.dao.*;
import org.bin2.jag.dao.query.builder.GenericQueryContextBuilder;
import org.bin2.jag.dao.query.builder.ParameterHandlerFactory;
import org.bin2.jag.dao.query.builder.QueryHandlerFactory;
import org.bin2.jag.dao.query.builder.ResultHandlerFactory;
import org.bin2.jag.dao.query.sfm.JdbcQuery;
import org.bin2.jag.dao.query.sfm.NoActionParameterHandler;

import java.lang.annotation.Annotation;
import java.sql.Connection;

/**
 * standard and default queryContextBuilder use handlerFactory to build the
 * context, a factory for an annotation, and a factory if no matching annotation
 * found //TODO add comment on customization
 */
public class JdbcQueryContextBuilder<T> extends GenericQueryContextBuilder<JdbcQuery,Connection, T> {


    /**
	 * build a default QueryContextBuilder with standard handlerFactories, it
	 * manage the following annotations by default
	 * <ul>
	 * <li>Query
	 * <ul>
	 * <li>{@see NamedQuery}</li>
	 * <li>{@see Query}</li>
	 * </ul>
	 * </li>
	 * <li>Parameters
	 * <ul>
	 * <li>{@see NamedParameter}</li>
	 * <li>{@see FetchSize}</li>
	 * <li>{@see FirstResult}</li>
	 * </ul>
	 * </li>
	 * <li>Result type managed
	 * <ul>
	 * <li>{@see List}</li>
	 * <li>{@see Iterator}</li>
	 * <li>{@see org.hibernate.Query#uniqueResult}</li>
	 * </ul>
	 * </li>
	 * </ul>
	 */
    public JdbcQueryContextBuilder() {
        super(ImmutableMap.<Class<? extends Annotation>, QueryHandlerFactory>builder().put(Query.class, new QueryHandlerFactoryImpl()).build(),
                new NamedQueryHandlerFactoryImpl(),
                ImmutableMap.<Class<? extends Annotation>, ParameterHandlerFactory>builder()
                        .put(IndexParameter.class, new IndexParameterHandlerFactory())
                        .put(FirstResult.class, new FirstResultParameterHandlerFactory())
                        .put(MaxResult.class, new MaxResultParameterHandlerFactory())
                        .put(FetchSize.class, new FetchSizeParameterHandlerFactory())
                                        .build(),
                ImmutableMap.<Class<? extends Annotation>, ResultHandlerFactory>builder().build(),
                new StandardResultHandlerFactory(),
                NoActionParameterHandler.INSTANCE);
    }


}
