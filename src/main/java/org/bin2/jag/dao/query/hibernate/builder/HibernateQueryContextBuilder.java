package org.bin2.jag.dao.query.hibernate.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.FetchSize;
import org.bin2.jag.dao.FirstResult;
import org.bin2.jag.dao.MaxResult;
import org.bin2.jag.dao.NamedParameter;
import org.bin2.jag.dao.NamedQuery;
import org.bin2.jag.dao.Query;
import org.bin2.jag.dao.query.builder.*;
import org.bin2.jag.dao.query.hibernate.NoActionParameterHandler;
import org.bin2.jag.dao.query.ParameterHandler;
import org.bin2.jag.dao.query.QueryContext;
import org.bin2.jag.dao.query.QueryHandler;
import org.bin2.jag.dao.query.ResultHandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hibernate.Session;

/**
 * standard and default queryContextBuilder use handlerFactory to build the
 * context, a factory for an annotation, and a factory if no matching annotation
 * found //TODO add comment on customization
 */
public class HibernateQueryContextBuilder<T> extends GenericQueryContextBuilder<org.hibernate.Query,Session, T> {


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
    public HibernateQueryContextBuilder() {
        super(ImmutableMap.<Class<? extends Annotation>, QueryHandlerFactory>builder().put(Query.class, new QueryHandlerFactoryImpl()).build(),
                new NamedQueryHandlerFactoryImpl(),
                ImmutableMap.<Class<? extends Annotation>, ParameterHandlerFactory>builder()
                        .put(NamedParameter.class, new NamedParameterHandlerFactory())
                        .put(FirstResult.class, new FirstResultParameterHandlerFactory())
                        .put(MaxResult.class, new MaxResultParameterHandlerFactory())
                        .put(FetchSize.class, new FetchSizeParameterHandlerFactory())
                                        .build(),
                ImmutableMap.<Class<? extends Annotation>, ResultHandlerFactory>builder().build(),
                new StandardResultHandlerFactory(),
                NoActionParameterHandler.INSTANCE);
    }


}
