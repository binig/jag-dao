package org.bin2.jag.dao.query.hibernate;

import org.bin2.jag.dao.query.hibernate.IndexedParameterHandler;
import org.hibernate.Query;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.Test;

/**
 * @author broger
 *         Date: 12/02/12
 *         Time: 11:35
 */
public class IndexedParameterHandlerTest {

    @Test
    public void testIndexedParameterHandlerTest() {
        final int idx = 10;

        final String param = "Param";
        IndexedParameterHandler h = new IndexedParameterHandler(idx);
        Mockery ctx = new Mockery();
        final Query query = ctx.mock(Query.class);
        ctx.checking(new Expectations() {{
            one(query).setParameter(idx, param);
        }});
        h.proceedParameter(query, param);
        ctx.assertIsSatisfied();
    }
}
