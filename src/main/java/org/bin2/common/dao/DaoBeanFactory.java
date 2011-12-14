package org.bin2.common.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;

public class DaoBeanFactory implements FactoryBean<Dao<?, ?>> {
	private boolean singleton;
	/**
	 * the dao class to implements
	 */
	private Class<? extends Dao<?, ? extends Serializable>> dao;

	/**
	 * optional persistentClass
	 * if not provided will check the parametized type of the DAO
	 */
	private Class<?> persistentClass;

	/**
	 * the session factory
	 */
	private SessionFactory sessionFactory;

	public Class<? extends Dao<?, ? extends Serializable>> getDao() {
		return this.dao;
	}

	@Override
	public Dao<?, ?> getObject() throws Exception {
		if (this.persistentClass==null) {
			tryingExtractPersistentType();
		}
		final DaoProxyHandler handler = new DaoProxyHandler(
				this.sessionFactory, this.dao, this.persistentClass);
		return Dao.class
		.cast(Proxy.newProxyInstance(Thread.currentThread()
				.getContextClassLoader(), new Class<?>[] { this.dao },
				handler));
	}

	@Override
	public Class<? extends Dao<?, ? extends Serializable>> getObjectType() {
		return this.dao;
	}

	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	@Override
	public boolean isSingleton() {
		return this.singleton;
	}

	public void setDao(final Class<? extends Dao<?, ? extends Serializable>> dao) {
		this.dao = dao;
	}

	public void setPersistent(final Class<?> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setSingleton(final boolean singleton) {
		this.singleton = singleton;
	}

	private void tryingExtractPersistentType() {
		// try to detect the type of the dao
		final Type[] types = dao.getGenericInterfaces();
		if (types !=null) {
			for (final Type type : dao.getGenericInterfaces()){
				if (type instanceof ParameterizedType) {
					final ParameterizedType p = (ParameterizedType)type;
					final Class<?> rtype = (Class<?>)p.getRawType();
					if (Dao.class.isAssignableFrom(rtype)) {
						this.persistentClass = (Class<?>)p.getActualTypeArguments()[0];
						break;
					}
				}
			}}
		if(this.persistentClass==null) {
			throw new RuntimeException("cannot automatically resolve persistentClass, please set the property manually");
		}
	}

}
