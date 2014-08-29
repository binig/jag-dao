package org.bin2.jag.dao.query.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bin2.jag.dao.*;
import org.bin2.jag.dao.query.ParameterHandler;
import org.bin2.jag.dao.query.QueryContext;
import org.bin2.jag.dao.query.QueryHandler;
import org.bin2.jag.dao.query.ResultHandler;
import org.bin2.jag.dao.query.hibernate.NoActionParameterHandler;
import org.bin2.jag.dao.query.hibernate.builder.*;
import org.hibernate.Session;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * standard and default queryContextBuilder use handlerFactory to build the
 * context, a factory for an annotation, and a factory if no matching annotation
 * found //TODO add comment on customization
 */
public class GenericQueryContextBuilder<Q,S, T> implements QueryContextBuilder<Q,S, T> {
	private final Map<Class<? extends Annotation>, QueryHandlerFactory> queryHandlerFactoryMap;
	private final QueryHandlerFactory<?> noQueryAnnotationFactory;

	private final Map<Class<? extends Annotation>, ParameterHandlerFactory> parameterHandlerFactoryMap;

	private final Map<Class<? extends Annotation>, ResultHandlerFactory> resultHandlerFactoryMap;
	private final ResultHandlerFactory<?> noAnnotationResultHandlerFactory;
    private final ParameterHandler<Q> noActionParameterHandler;

    public GenericQueryContextBuilder(Map<Class<? extends Annotation>, QueryHandlerFactory> queryHandlerFactoryMap, QueryHandlerFactory<?> noQueryAnnotationFactory, Map<Class<? extends Annotation>, ParameterHandlerFactory> parameterHandlerFactoryMap, Map<Class<? extends Annotation>, ResultHandlerFactory> resultHandlerFactoryMap,
                                      ResultHandlerFactory<?> noAnnotationResultHandlerFactory,
                                      ParameterHandler<Q> noActionParameterHandler) {
        this.queryHandlerFactoryMap = queryHandlerFactoryMap;
        this.noQueryAnnotationFactory = noQueryAnnotationFactory;
        this.parameterHandlerFactoryMap = parameterHandlerFactoryMap;
        this.resultHandlerFactoryMap = resultHandlerFactoryMap;
        this.noAnnotationResultHandlerFactory = noAnnotationResultHandlerFactory;
        this.noActionParameterHandler = noActionParameterHandler;
    }

    @Override
	public Map<Method, QueryContext<Q,S>> buildQueryContexts(final Class<T> persistentClass, final Class<? extends Dao> daoClass) {
		final Map<Method, QueryContext<Q,S>> contexts = Maps.newHashMap();
		for (final Method m : daoClass.getMethods()) {
			contexts.put(m, buildContextForMethod(m,persistentClass, daoClass));
		}
		return contexts;
	}

	private QueryContext buildContextForMethod(final Method m,final Class<T> persistentClass, final Class<? extends Dao> daoClass) {
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
		return parameterHandler == null ? this.noActionParameterHandler : parameterHandler;
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
