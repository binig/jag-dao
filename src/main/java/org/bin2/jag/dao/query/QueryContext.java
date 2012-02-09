package org.bin2.jag.dao.query;

import java.util.List;

public class QueryContext {
    private final QueryHandler queryHandler;
    private final ResultHandler resultHandler;
    private final List<ParameterHandler> parameterHandlers;
   
    public QueryContext(QueryHandler queryHandler,ResultHandler resultHandler,List<ParameterHandler> parameterHandlers) {
        this.queryHandler=  queryHandler;
        this.resultHandler= resultHandler;
        this.parameterHandlers = parameterHandlers;
    }

    public QueryHandler getQueryHandler() {
       return queryHandler;
    }
    
    public ResultHandler getResultHandler() {
       return resultHandler;
    }

    public List<ParameterHandler>  getParameterHandlers() {
       return parameterHandlers;
    }
}
