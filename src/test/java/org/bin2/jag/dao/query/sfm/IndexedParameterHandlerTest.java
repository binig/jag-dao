package org.bin2.jag.dao.query.sfm;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author broger
 *         Date: 12/02/12
 *         Time: 11:35
 */
public class IndexedParameterHandlerTest {

    @Test
    public void testIndexedParameterHandlerTest() throws Exception{
        final int idx = 10;

        final String param = "Param";
        IndexedParameterHandler h = new IndexedParameterHandler(idx);
        Mockery ctx = new Mockery();
        final Connection connection = ctx.mock(Connection.class);
        final PreparedStatement st = ctx.mock(PreparedStatement.class);
        ctx.checking(new Expectations() {{
            one(connection).prepareStatement("");
            will(returnValue(st));
            one(st).setObject(idx, param);
        }});
        JdbcQuery<?> query = new JdbcQuery<Object>("",connection,null);
        h.proceedParameter(query, param);
        ctx.assertIsSatisfied();
    }
}
