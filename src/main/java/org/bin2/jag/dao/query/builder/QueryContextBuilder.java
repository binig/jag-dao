package org.bin2.jag.dao.query.builder;

import org.bin2.jag.dao.query.QueryContext;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * build a query context by extrating annotation information on the dao class
 * the build query context is used by the DaoProxyHandler to execute the query
 *
 * @author broger
 *         Date: 11/02/12
 *         Time: 18:34
 * @see org.bin2.jag.dao.DaoProxyHandler
 * @see org.bin2.jag.dao.DaoBeanFactory#DaoBeanFactory(QueryContextBuilder)
 */
public interface QueryContextBuilder {
    /**
     * build a query context by extrating annotation information on the dao class
     * the build query context is used by the DaoProxyHandler to execute the query
     *
     * @param daoClass the daoClass
     * @return the map of QueryContext by method
     * @see org.bin2.jag.dao.DaoProxyHandler
     * @see org.bin2.jag.dao.DaoBeanFactory#DaoBeanFactory(QueryContextBuilder)
     */
    Map<Method, QueryContext> buildQueryContexts(Class<?> daoClass);
}
