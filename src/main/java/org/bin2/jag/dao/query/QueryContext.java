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
public class QueryContext<Q,S> {
    private final QueryHandler<Q,S> queryHandler;
    private final ResultHandler<Q,?> resultHandler;
    private final List<ParameterHandler<Q>> parameterHandlers;

    /**
     * @param queryHandler the query handler used to build the query 
     * @param resultHandler the result handler to build/transform the result from the query
     * @param parameterHandlers the method parameters handlers one by method args (put in an ImmutableList)
     * @see ImmutableList
     **/
    public QueryContext(QueryHandler<Q,S> queryHandler, ResultHandler<Q,?> resultHandler, List<ParameterHandler<Q>> parameterHandlers) {
        this.queryHandler = queryHandler;
        this.resultHandler = resultHandler;
        this.parameterHandlers = ImmutableList.copyOf(parameterHandlers);
    }
    /**
     * @return the query handler used to build the query
    */
    public QueryHandler<Q,S> getQueryHandler() {
        return queryHandler;
    }

    /**
     * @return the result handler to build/transform the result from the query
    */
    public ResultHandler<Q,?> getResultHandler() {
        return resultHandler;
    }

    /**
     * @return an immutableList of parameters  handlers
     * @see ImmutableList
    */
    public List<ParameterHandler<Q>> getParameterHandlers() {
        return parameterHandlers;
    }
}
