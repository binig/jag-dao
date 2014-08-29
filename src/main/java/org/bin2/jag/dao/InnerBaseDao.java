package org.bin2.jag.dao;

/**
 * Created by benoitroger on 29/08/14.
 */
public interface InnerBaseDao<T,PK ,S> {

    void setPersistentClass(Class<T> clazz);

    /**
     * save to db a new instance of T
     * @param t object to be saved
     **/
    void create(T t);

    /**
     * @param t object to be deleted
     */
    void delete(T t);

    /**
     * load and return the object according to its primaryKey
     * @param primaryKey the primaryKey of the object to load
     * @return the found object with the matched primaryKey
     * @see org.hibernate.Session#load
     */
    T load(PK primaryKey);

    /**
     *
     * @return the session/connection object
     */
    S getSession();
}
