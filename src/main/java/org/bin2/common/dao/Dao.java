package org.bin2.common.dao;

import java.io.Serializable;

public interface Dao<T, PK extends Serializable> {

	void create(T t);

	void delete(T t);

	T load(PK pimaryKey);
}
