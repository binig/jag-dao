package org.bin2.jag.dao.query.sfm;

import com.google.common.base.Throwables;
import org.sfm.jdbc.JdbcMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Created by benoitroger on 29/08/14.
 */
public class JdbcIterator<T> implements AutoCloseable, Iterator<T> {
    private final ResultSet resultSet;
    private final JdbcMapper<T> mapper;
    private boolean didNext = false;
    private boolean hasNext = false;

    public JdbcIterator(ResultSet resultSet, JdbcMapper<T> mapper) {
        this.resultSet = resultSet;
        this.mapper = mapper;
    }

    @Override
    public void close() throws Exception {
        resultSet.close();
    }

    @Override
    public boolean hasNext() {
        if (!didNext) {
            hasNext = resultSetNext();
            didNext = true;
        }
        return hasNext;
    }

    private boolean resultSetNext() {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw Throwables.propagate(e);
        }

    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T next() {
        if (!didNext) {
            resultSetNext();
        }
        didNext = false;
        try {
            return mapper.map(resultSet);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
