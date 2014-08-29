package org.bin2.jag.dao;

import org.bin2.jag.dao.query.builder.QueryContextBuilder;
import org.bin2.jag.dao.query.hibernate.builder.HibernateQueryContextBuilder;
import org.bin2.jag.dao.query.sfm.JdbcQuery;
import org.bin2.jag.dao.query.sfm.builder.JdbcQueryContextBuilder;
import org.hibernate.Query;
import org.hibernate.Session;

import java.io.Serializable;
import java.sql.Connection;

/**
 * Created by benoitroger on 29/08/14.
 */
public class SfmDaoFactory<T,PK extends Serializable> extends DaoBeanFactory<JdbcQuery,Connection,T, PK> {
    public SfmDaoFactory() {
        super(new JdbcQueryContextBuilder());
    }

}
