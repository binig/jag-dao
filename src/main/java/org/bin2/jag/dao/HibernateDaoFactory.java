package org.bin2.jag.dao;

import org.bin2.jag.dao.query.builder.QueryContextBuilder;
import org.bin2.jag.dao.query.hibernate.builder.HibernateQueryContextBuilder;
import org.hibernate.*;
import org.hibernate.Query;

import java.io.Serializable;

/**
 * Created by benoitroger on 29/08/14.
 */
public class HibernateDaoFactory<T,PK extends Serializable> extends DaoBeanFactory<org.hibernate.Query,Session,T, PK> {
    public HibernateDaoFactory() {
        super(new HibernateQueryContextBuilder());
    }

    public HibernateDaoFactory(QueryContextBuilder<Query, Session, T> queryContextBuilder) {
        super(queryContextBuilder);
    }
}
