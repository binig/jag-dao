package org.bin2.jag.dao;

import java.io.Serializable;

/**
 * Dao interface, extends this interface to build your own dao
 * in your extension add a method by query and user the Query, NamedParameter ... annotation
 * to specified the query for the method
 * @see DaoBeanFactory
 * @see Query 
 * @see NamedParameter
 * @see FirstResult
 * @see FetchSize 
 * @param <T> type of the persisted class to be managed by this Dao
 * @param <PK> type of the primary key of the persisted class to be managed by this Dao
 **/
public interface Dao<T, PK extends Serializable> {

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
}
