package org.bin2.jag.dao.query.sfm;

import junit.framework.Assert;
import org.bin2.jag.dao.SfmDaoFactory;
import org.hsqldb.jdbc.JDBCDriver;
import org.junit.Test;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.util.List;

/**
 * Created by benoitroger on 29/08/14.
 */
public class SfmDaoTest {

    @Test
    public void test() throws Exception{
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(JDBCDriver.class);
        dataSource.setUrl("jdbc:hsqldb:mem:mymemdb");
        dataSource.setUsername("SA");
        dataSource.getConnection().prepareStatement(
                "CREATE TABLE PUBLIC.MYTABLE\n" +
                        "(id INTEGER NOT NULL,\n" +
                        "email VARCHAR(25),\n" +
                        "MY_PROPERTY VARCHAR(25),\n" +
                        "PRIMARY KEY (id)) "
        ).executeUpdate();
        JdbcInnerBaseDao<MyObject,Long> inner = new JdbcInnerBaseDao<MyObject, Long>(dataSource);
        SfmDaoFactory<MyObject, Long > factory = new SfmDaoFactory<MyObject, Long>();
        factory.setInnerBaseDao(inner);
        factory.setDao(SfmDao.class);
        SfmDao dao = (SfmDao)factory.getObject();
        Assert.assertTrue(dao.findAll().isEmpty());
        dataSource.getConnection().prepareStatement(
                "insert into  PUBLIC.MYTABLE values(1,'email1', 'prop1' ) "
        ).executeUpdate();
        dataSource.getConnection().prepareStatement(
                "insert into  PUBLIC.MYTABLE values(2,'email2', 'prop2' ) "
        ).executeUpdate();
        List<MyObject> objects = dao.findAll();
        Assert.assertEquals(objects.size(),2);
        Assert.assertEquals("email2",objects.get(1).getEmail());

        MyObject m = dao.findByEmail("email2");
        Assert.assertEquals(m.getId(),2);


    }
}
