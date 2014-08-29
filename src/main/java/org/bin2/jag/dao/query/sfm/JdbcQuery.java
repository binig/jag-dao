package org.bin2.jag.dao.query.sfm;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.sfm.jdbc.JdbcMapper;
import org.sfm.jdbc.JdbcMapperFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by benoitroger on 29/08/14.
 */
public class JdbcQuery<R> {
    private PreparedStatement statement;
    private List<PrefetchAction> prefetchActions;
    private JdbcMapper<R> mapper;
    public JdbcQuery(String query, Connection connection, JdbcMapper<R> mapper) {
        try {
            this.statement = connection.prepareStatement(query);
        } catch (SQLException e) {
            throw Throwables.propagate(e);
        }
        this.prefetchActions = Lists.newArrayList();
        this.mapper = mapper;
    }

    public PreparedStatement getStatement() {
        return statement;
    }

    public void addPrefetchAction(PrefetchAction action ){
        this.prefetchActions.add(action);
    }

    public List<PrefetchAction> getPrefetchActions() {
        return prefetchActions;
    }

    public ResultSet  performQuery() {
        try {
            ResultSet rs = statement.executeQuery();
            for (PrefetchAction a:this.prefetchActions) {
                    a.execute(rs);
            }
            return  rs;
        } catch (SQLException e) {
            throw Throwables.propagate(e);
        }
    }

    public JdbcMapper<R> getMapper() {
        return mapper;
    }
}
