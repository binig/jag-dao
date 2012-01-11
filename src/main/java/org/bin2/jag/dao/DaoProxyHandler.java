package org.bin2.jag.dao;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

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

	public DaoProxyHandler(final SessionFactory sessionFactory,
			final Class<?> daoClass, final Class<?> persistentClass) {
		super();
		this.sessionFactory = sessionFactory;
		this.daoClass = daoClass;
		this.persistentClass = persistentClass;
	}

	public void create(final Object o) {
		this.sessionFactory.getCurrentSession().save(o);

	}

	public void delete(final Object o) {
		this.sessionFactory.getCurrentSession().delete(o);

	}

	public Object executeNamedQuery(final Method m, final Object[] args) {
		final StringBuffer queryName = new StringBuffer(this.daoClass
				.getSimpleName());
		queryName.append(".").append(m.getName());
		final org.bin2.jag.dao.Query q = m
				.getAnnotation(org.bin2.jag.dao.Query.class);

		final Query query = q != null ? this.sessionFactory.getCurrentSession()
				.createQuery(q.value()) : this.sessionFactory
				.getCurrentSession().getNamedQuery(queryName.toString());
		final Annotation[][] annotations = m.getParameterAnnotations();
		for (int i = 0; args != null && i < args.length; i++) {
			final NamedParameter annot = getParameterAnnotation(annotations, i,
					NamedParameter.class);
			if (annot != null) {
				query.setParameter(annot.value(), args[i]);
			} else if (getParameterAnnotation(annotations, i, FetchSize.class) != null) {
				query.setFetchSize(((Number) args[i]).intValue());
			} else if (getParameterAnnotation(annotations, i, FirstResult.class) != null) {
				query.setFirstResult(((Number) args[i]).intValue());
			} else {
				DaoProxyHandler.LOGGER
						.warn(
								"{}  ignoring parameter {}  no annotation to defined its role",
								queryName, i);
			}
		}
		final Object result;
		if (m.getReturnType().isAssignableFrom(List.class)) {
			result = query.list();
		} else if (m.getReturnType().isAssignableFrom(Iterator.class)) {
			result = query.iterate();
		} else {
			result = query.uniqueResult();
		}
		return result;
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
