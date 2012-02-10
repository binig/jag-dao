package org.bin2.jag.dao.query;

import com.google.common.collect.ImmutableList;

import java.util.List;
/**
 * Handle all the information of a daoMethod.
 * Used by the DaoProxyHandler to compute the query.
 * @see org.bin2.jag.dao.DaoProxyHandler
 * @see org.bin2.jag.dao.Dao
 * @see org.bin2.jag.dao.DaoBeanFactory
 **/
public class QueryContext {
    private final QueryHandler queryHandler;
    private final ResultHandler resultHandler;
    private final List<ParameterHandler> parameterHandlers;

    /**
     * @param queryHandler the query handler used to build the query 
     * @param resultHandler the result handler to build/transform the result from the query
     * @param parameterHandlers the method parameters handlers one by method args (put in an ImmutableList)
     * @see ImmutableList
     **/
    public QueryContext(QueryHandler queryHandler, ResultHandler resultHandler, List<ParameterHandler> parameterHandlers) {
        this.queryHandler = queryHandler;
        this.resultHandler = resultHandler;
        this.parameterHandlers = ImmutableList.copyOf(parameterHandlers);
    }
    /**
     * @return the query handler used to build the query
    */
    public QueryHandler getQueryHandler() {
        return queryHandler;
    }

    /**
     * @return the result handler to build/transform the result from the query
    */
    public ResultHandler getResultHandler() {
        return resultHandler;
    }

    /**
     * @return an immutableList of parameters  handlers
     * @see ImmutableList
    */
    public List<ParameterHandler> getParameterHandlers() {
        return parameterHandlers;
    }
}
