package org.bin2.jag.dao.query.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.bin2.jag.dao.Dao;
import org.bin2.jag.dao.FetchSize;
import org.bin2.jag.dao.FirstResult;
import org.bin2.jag.dao.MaxResult;
import org.bin2.jag.dao.NamedParameter;
import org.bin2.jag.dao.NamedQuery;
import org.bin2.jag.dao.Query;
import org.bin2.jag.dao.query.NoActionParameterHandler;
import org.bin2.jag.dao.query.ParameterHandler;
import org.bin2.jag.dao.query.QueryContext;
import org.bin2.jag.dao.query.QueryHandler;
import org.bin2.jag.dao.query.ResultHandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * standard and default queryContextBuilder use handlerFactory to build the
 * context, a factory for an annotation, and a factory if no matching annotation
 * found //TODO add comment on customization
 */
public class StandardQueryContextBuilder implements QueryContextBuilder {
	private final Map<Class<? extends Annotation>, QueryHandlerFactory> queryHandlerFactoryMap;
	private final QueryHandlerFactory<?> noQueryAnnotationFactory;

	private final Map<Class<? extends Annotation>, ParameterHandlerFactory> parameterHandlerFactoryMap;

	private final Map<Class<? extends Annotation>, ResultHandlerFactory> resultHandlerFactoryMap;
	private final ResultHandlerFactory<?> noAnnotationResultHandlerFactory;

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
	public StandardQueryContextBuilder() {
		this.queryHandlerFactoryMap = Maps.newHashMap();
		this.queryHandlerFactoryMap.put(Query.class, new QueryHandlerFactoryImpl());
		this.noQueryAnnotationFactory = new NamedQueryHandlerFactoryImpl();
		this.queryHandlerFactoryMap.put(NamedQuery.class, this.noQueryAnnotationFactory);
		this.parameterHandlerFactoryMap = Maps.newHashMap();
		this.parameterHandlerFactoryMap.put(NamedParameter.class, new NamedParameterHandlerFactory());
		this.parameterHandlerFactoryMap.put(FirstResult.class, new FirstResultParameterHandlerFactory());
		this.parameterHandlerFactoryMap.put(MaxResult.class, new MaxResultParameterHandlerFactory());
		this.parameterHandlerFactoryMap.put(FetchSize.class, new FetchSizeParameterHandlerFactory());
		this.resultHandlerFactoryMap = Maps.newHashMap();
		this.noAnnotationResultHandlerFactory = new StandardResultHandlerFactory();
	}

	@Override
	public Map<Method, QueryContext> buildQueryContexts(final Class<? extends Dao> daoClass) {
		final Map<Method, QueryContext> contexts = Maps.newHashMap();
		for (final Method m : daoClass.getMethods()) {
			contexts.put(m, buildContextForMethod(m, daoClass));
		}
		return contexts;
	}

	private QueryContext buildContextForMethod(final Method m, final Class<? extends Dao> daoClass) {
		final QueryHandler queryHandler = buildQueryHandler(m, daoClass);
		final ResultHandler result = buildResultHandler(m, daoClass);
		final List<ParameterHandler> parameterHandlers = Lists.newArrayList();
		final Annotation[][] annotations = m.getParameterAnnotations();
		for (int i = 0; i < annotations.length; i++) {
			parameterHandlers.add(buildParameterHandler(annotations[i], m, daoClass, i));
		}
		return new QueryContext(queryHandler, result, parameterHandlers);

	}

	private ParameterHandler buildParameterHandler(final Annotation[] annotations, final Method m, final Class<? extends Dao> daoClass, final int idx) {
		ParameterHandler parameterHandler = null;
		for (final Map.Entry<Class<? extends Annotation>, ParameterHandlerFactory> entry : this.parameterHandlerFactoryMap.entrySet()) {
			final Annotation a = getParameterAnnotation(annotations, entry.getKey());
			if (a != null) {
				parameterHandler = entry.getValue().build(a, daoClass, m, idx);
				break;
			}
		}
		return parameterHandler == null ? NoActionParameterHandler.INSTANCE : parameterHandler;
	}

	private QueryHandler buildQueryHandler(final Method m, final Class<? extends Dao> daoClass) {
		QueryHandler h = null;
		for (final Map.Entry<Class<? extends Annotation>, QueryHandlerFactory> entry : this.queryHandlerFactoryMap.entrySet()) {
			final Annotation a = m.getAnnotation(entry.getKey());
			if (a != null) {
				h = entry.getValue().build(a, daoClass, m);
				break;
			}
		}
		if (h == null) {
			h = this.noQueryAnnotationFactory.build(null, daoClass, m);
		}
		return h;
	}

	private ResultHandler buildResultHandler(final Method m, final Class<? extends Dao> daoClass) {
		ResultHandler h = null;
		for (final Map.Entry<Class<? extends Annotation>, ResultHandlerFactory> entry : this.resultHandlerFactoryMap.entrySet()) {
			final Annotation a = m.getAnnotation(entry.getKey());
			if (a != null) {
				h = entry.getValue().build(a, daoClass, m);
				break;
			}
		}
		if (h == null) {
			h = this.noAnnotationResultHandlerFactory.build(null, daoClass, m);
		}
		return h;
	}

	private <T extends Annotation> T getParameterAnnotation(final Annotation[] annots, final Class<T> annoClass) {
		final T p;
		T found = null;
		for (final Annotation a : annots) {
			if (a.annotationType().isAssignableFrom(annoClass)) {
				found = annoClass.cast(a);
			}
		}
		if (found != null) {
			p = found;
		} else {
			p = null;
		}

		return p;
	}

}
