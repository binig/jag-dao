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

    /**
     * will use namequery TestDao.findByCode2
     *
     * @param uselessParam
     * @return
     */
    @NamedQuery()
    List<BigDecimal> findByCode2(String uselessParam, @FetchSize int fetchSize,
                                 @FirstResult int offset);

    /**
     * will use namequery FindByMyAnnot
     *
     * @param uselessParam
     * @return
     */
    @NamedQuery("FindByMyAnnot")
    List<BigDecimal> findByCode3(String uselessParam, @FetchSize int fetchSize,
                                 @FirstResult int offset);

    BigDecimal findById();
}
