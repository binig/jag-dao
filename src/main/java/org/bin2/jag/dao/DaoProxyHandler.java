package org.bin2.jag.dao;

import com.google.common.collect.Maps;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bin2.jag.dao.query.*;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DaoProxyHandler implements InvocationHandler {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DaoProxyHandler.class);
	private final SessionFactory sessionFactory;
	private final Class<?> daoClass;
	private final Class<?> persistentClass;
        private final Map<Method, QueryContext> queryContexts;
	public DaoProxyHandler(final SessionFactory sessionFactory,
			final Class<?> daoClass, final Class<?> persistentClass) {
		super();
		this.sessionFactory = sessionFactory;
		this.daoClass = daoClass;
		this.persistentClass = persistentClass;
                this.queryContexts = buildQueryContexts(daoClass);
	}
        
        private Map<Method, QueryContext> buildQueryContexts(Class<?> daoClass) {
             Map<Method, QueryContext> contexts = Maps.newHashMap();
             for(Method m: daoClass.getMethods()) {
                  contexts.put(m,buildContextForMethod(m));
             }
             return contexts;
        }

	public void create(final Object o) {
		this.sessionFactory.getCurrentSession().save(o);

	}

	public void delete(final Object o) {
		this.sessionFactory.getCurrentSession().delete(o);

	}

        private QueryContext buildContextForMethod(Method m) {
		final StringBuffer queryName = new StringBuffer(this.daoClass
				.getSimpleName());
		queryName.append(".").append(m.getName());
		final org.bin2.jag.dao.Query q = m
				.getAnnotation(org.bin2.jag.dao.Query.class);
		final QueryHandler queryHandler;
                if (q==null) {
                    queryHandler = new NamedQueryHandler(queryName.toString()); 
                }  else {
                   queryHandler = new BasicQueryHandler(q.value());
                }
                
               final ResultHandler result;
		if (m.getReturnType().isAssignableFrom(List.class)) {
			result = new ListResultHandler();
		} else if (m.getReturnType().isAssignableFrom(Iterator.class)) {
			result = new IteratorResultHandler();
		} else {
			result = new UniqueResultHandler();
		}
               
                List<ParameterHandler> parameterHandlers = Lists.newArrayList();
               final Annotation[][] annotations = m.getParameterAnnotations();
               int idxCmpt =0;
                for (int i = 0; i<annotations.length; i++) {
                        final NamedParameter annot = getParameterAnnotation(annotations, i,
                                        NamedParameter.class);
                        ParameterHandler parameterHandler=null;
                        if (annot != null) {
                                parameterHandler= new NamedParameterHandler(annot.value());
                        } else if (getParameterAnnotation(annotations, i, FetchSize.class) != null) {
                                parameterHandler= new FetchSizeParameterHandler();
                        } else if (getParameterAnnotation(annotations, i, FirstResult.class) != null) {
                                parameterHandler= new FirstResultParameterHandler();
                        } else {
                             //  parameterHandler= new IndexedParameterHandler(idxCmpt);
                               idxCmpt++;
                        }
                        parameterHandlers.add(parameterHandler);
                }
               return new QueryContext(queryHandler, result, parameterHandlers);

	}
	public Object executeNamedQuery(final Method m, final Object[] args) {
                QueryContext ctx = this.queryContexts.get(m);
                Query query = ctx.getQueryHandler().getQuery(this.sessionFactory.getCurrentSession()); 
		for (int i = 0; args != null && i < args.length; i++) {
			ParameterHandler ph  = ctx.getParameterHandlers().get(i);
                        if (ph!=null) {
                             ph.proceedParameter(query,args[i]);
			}
		}
		return ctx.getResultHandler().result(query);
	}

	private <T extends Annotation> T getParameterAnnotation(
			final Annotation[][] annots, final int i, final Class<T> annoClass) {
		final T p;
		if (i < annots.length && annots[i] != null) {
			T found = null;
			for (final Annotation a : annots[i]) {
				if (a.annotationType().isAssignableFrom(annoClass)) {
					found = annoClass.cast(a);
				}
			}
			if (found != null) {
				p = found;
			} else {
				p = null;
			}
		} else {
			p = null;
		}

		return p;
	}

	@Override
	public Object invoke(final Object o, final Method m, final Object[] args)
			throws Throwable {
		final Object result;
		if ("toString".equals(m.getName())) {
			return this.daoClass.getName();
		} else if ("load".equals(m.getName())) {
			result = load((Serializable) args[0]);
		} else if ("delete".equals(m.getName())) {
			result = null;
			delete(args[0]);
		} else if ("create".equals(m.getName())) {
			result = null;
			create(args[0]);
		} else {
			// namedquery case;
			result = executeNamedQuery(m, args);
		}
		return result;
	}

	public Object load(final Serializable id) {
		return this.sessionFactory.getCurrentSession().load(
				this.persistentClass, id);
	}
}
