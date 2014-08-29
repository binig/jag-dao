package org.bin2.jag.dao.query.hibernate;

import org.bin2.jag.dao.InnerBaseDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * Created by benoitroger on 29/08/14.
 */
public class HibernateInnerBaseDao<T,PK extends Serializable> implements InnerBaseDao<T,PK,Session> {
    /**
     * optional persistentClass
     * if not provided will check the parametized type of the DAO
     */
    private Class<T> persistentClass;

    /**
     * the session factory
     */
    private SessionFactory sessionFactory;

    public HibernateInnerBaseDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(T t) {
        getSession().save(t);

    }

    @Override
    public void delete(T t) {
        getSession().delete(t);

    }

    @Override
    public T load(PK primaryKey) {
        return (T)getSession().load(persistentClass, primaryKey);
    }

    @Override
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void setPersistentClass(Class<T> clazz) {
        this.persistentClass = clazz;
    }
}
