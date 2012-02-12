package org.bin2.jag.dao.query.builder;

import org.bin2.jag.dao.NamedQuery;
import org.bin2.jag.dao.TestDao;
import org.bin2.jag.dao.query.QueryHandler;
import org.hibernate.Session;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * @author broger
 *         Date: 12/02/12
 *         Time: 14:12
 */
public class NamedQueryHandlerFactoryImplTest {
    @Test(dataProvider = "queryData")
    public void testWithoutAnnotation(String method, final String queryName) {
        NamedQueryHandlerFactoryImpl f = new NamedQueryHandlerFactoryImpl();
        Method m = getMethod(TestDao.class, method);
        QueryHandler h = f.build(m.getAnnotation(NamedQuery.class), TestDao.class, m);
        Mockery ctx = new Mockery();
        final Session session = ctx.mock(Session.class);
        ctx.checking(new Expectations() {{
            one(session).getNamedQuery(queryName);

        }});
        h.getQuery(session);
        ctx.assertIsSatisfied();
    }

    @DataProvider
    public static Object[][] queryData() {
        return new Object[][]{{"findByCode2", "TestDao.findByCode2"}, {"findByCode3", "FindByMyAnnot"}};
    }

    public static Method getMethod(Class<?> cl, String method) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }
}
