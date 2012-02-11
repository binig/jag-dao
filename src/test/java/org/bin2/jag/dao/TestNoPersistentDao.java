package org.bin2.jag.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

public interface TestNoPersistentDao<T> extends Serializable, List<String> , Dao<T, Long> {

	@Query("select e from MyObject e where e.name like :name ")
	Iterator<BigDecimal> findByNames(@NamedParameter("name") String name);

	/**
	 * will use namequery TestDao.findByCode
	 * 
	 * @param uselessParam
	 * @return
	 */
	List<BigDecimal> findByCode(String uselessParam, @FetchSize int fetchSize,
                                @FirstResult int offset);

	BigDecimal findById();
}
