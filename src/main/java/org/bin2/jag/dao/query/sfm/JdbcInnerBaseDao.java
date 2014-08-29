package org.bin2.jag.dao.query.sfm;

import com.google.common.base.Throwables;
import org.bin2.jag.dao.InnerBaseDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by benoitroger on 29/08/14.
 */
public class JdbcInnerBaseDao<T,PK extends Serializable> implements InnerBaseDao<T,PK,Connection> {
    /**
     * optional persistentClass
     * if not provided will check the parametized type of the DAO
     */
    private Class<T> persistentClass;

    /**
     * the session factory
     */
    private DataSource dataSource;

    public JdbcInnerBaseDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(T t) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void delete(T t) {
        throw new UnsupportedOperationException();

    }

    @Override
    public T load(PK primaryKey) {

        throw new UnsupportedOperationException();

    }

    @Override
    public Connection getSession() {
        return DataSourceUtils.getConnection(dataSource);
    }

    @Override
    public void setPersistentClass(Class<T> clazz) {
        this.persistentClass = clazz;
    }
}
