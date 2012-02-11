package org.bin2.jag.dao.query.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bin2.jag.dao.FetchSize;
import org.bin2.jag.dao.FirstResult;
import org.bin2.jag.dao.NamedParameter;
import org.bin2.jag.dao.Query;
import org.bin2.jag.dao.query.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author broger
 *         Date: 11/02/12
 *         Time: 18:37
 */
public class StandardQueryContextBuilder implements QueryContextBuilder {
    public Map<Method, QueryContext> buildQueryContexts(Class<?> daoClass) {
        Map<Method, QueryContext> contexts = Maps.newHashMap();
        for (Method m : daoClass.getMethods()) {
            contexts.put(m, buildContextForMethod(m, daoClass));
        }
        return contexts;
    }

    private QueryContext buildContextForMethod(Method m, Class<?> daoClass) {
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

    private QueryHandler buildQueryHandler(Method m, Class<?> daoClass) {
        final StringBuilder queryName = new StringBuilder(daoClass
                .getSimpleName());
        queryName.append(".").append(m.getName());
        final Query q = m
                .getAnnotation(Query.class);
        final QueryHandler queryHandler;
        if (q == null) {
            queryHandler = new NamedQueryHandler(queryName.toString());
        } else {
            queryHandler = new BasicQueryHandler(q.value());
        }
        return queryHandler;
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
