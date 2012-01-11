package org.bin2.jag.dao;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

public interface TestDao extends Dao<BigDecimal, Long> {

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
