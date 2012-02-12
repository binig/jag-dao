package org.bin2.jag.dao.query.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bin2.jag.dao.*;
import org.bin2.jag.dao.query.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * standard and default queryContextBuilder
 * use handlerFactory to build the context, a factory for an annotation, and a factory if no matching annotation found
 * //TODO add comment on customization
 */
public class StandardQueryContextBuilder implements QueryContextBuilder {
    private final Map<Class<? extends Annotation>, QueryHandlerFactory> queryHandlerFactoryMap;
    private final QueryHandlerFactory<?> noQueryAnnotationFactory;

    /**
     * build a default QueryContextBuilder with standard handlerFactories,
     * it manage the following annotations by default
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
        queryHandlerFactoryMap = Maps.newHashMap();
        queryHandlerFactoryMap.put(Query.class, new QueryHandlerFactoryImpl());
        noQueryAnnotationFactory = new NamedQueryHandlerFactoryImpl();
        queryHandlerFactoryMap.put(NamedQuery.class, noQueryAnnotationFactory);
    }

    public Map<Method, QueryContext> buildQueryContexts(Class<? extends Dao> daoClass) {
        Map<Method, QueryContext> contexts = Maps.newHashMap();
        for (Method m : daoClass.getMethods()) {
            contexts.put(m, buildContextForMethod(m, daoClass));
        }
        return contexts;
    }

    private QueryContext buildContextForMethod(Method m, Class<? extends Dao> daoClass) {
        final QueryHandler queryHandler = buildQueryHandler(m, daoClass);
        final ResultHandler result = buildResultHandler(m);
        List<ParameterHandler> parameterHandlers = Lists.newArrayList();
        final Annotation[][] annotations = m.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            parameterHandlers.add(buildParameterHandler(annotations[i]));
        }
        return new QueryContext(queryHandler, result, parameterHandlers);

    }

    private ParameterHandler buildParameterHandler(Annotation[] annotations) {
        final NamedParameter annot = getParameterAnnotation(annotations,
                NamedParameter.class);
        ParameterHandler parameterHandler = null;
        if (annot != null) {
            parameterHandler = new NamedParameterHandler(annot.value());
        } else if (getParameterAnnotation(annotations, FetchSize.class) != null) {
            parameterHandler = new FetchSizeParameterHandler();
        } else if (getParameterAnnotation(annotations, FirstResult.class) != null) {
            parameterHandler = new FirstResultParameterHandler();
        }
        //else {
        // IndexedParam not implemented ... maybe latter if i find a good strategy ....
        //  parameterHandler= new IndexedParameterHandler(i);
        //}
        return parameterHandler == null ? NoActionParameterHandler.INSTANCE : parameterHandler;
    }

    private QueryHandler buildQueryHandler(Method m, Class<? extends Dao> daoClass) {
        QueryHandler h = null;
        for (Map.Entry<Class<? extends Annotation>, QueryHandlerFactory> entry : queryHandlerFactoryMap.entrySet()) {
            Annotation a = m.getAnnotation(entry.getKey());
            if (a != null) {
                h = entry.getValue().build(a, daoClass, m);
                break;
            }
        }
        if (h == null) {
            h = noQueryAnnotationFactory.build(null, daoClass, m);
        }
        return h;
    }

    private ResultHandler buildResultHandler(Method m) {
        final ResultHandler result;
        if (m.getReturnType().isAssignableFrom(List.class)) {
            result = new ListResultHandler();
        } else if (m.getReturnType().isAssignableFrom(Iterator.class)) {
            result = new IteratorResultHandler();
        } else {
            result = new UniqueResultHandler();
        }
        return result;
    }

    private <T extends Annotation> T getParameterAnnotation(
            final Annotation[] annots, final Class<T> annoClass) {
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
