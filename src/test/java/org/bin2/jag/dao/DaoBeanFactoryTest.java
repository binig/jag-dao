package org.bin2.jag.dao;

import com.google.common.collect.ImmutableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

public class DaoBeanFactoryTest {
    private DaoBeanFactory daoBeanFactory;
    private Mockery context;
    private SessionFactory sessionFactory;
    private Session session;

    @BeforeMethod
    public void setUp() {
        this.daoBeanFactory = new DaoBeanFactory();
        this.context = new Mockery();
        this.sessionFactory = this.context.mock(SessionFactory.class);
        this.session = this.context.mock(Session.class);
        this.daoBeanFactory.setSessionFactory(this.sessionFactory);
        this.daoBeanFactory.setDao(TestDao.class);

    }

    @AfterMethod
    public void tearDown() {
        this.context.assertIsSatisfied();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNoPersistentType() throws Exception {
        daoBeanFactory.setDao((Class<? extends Dao<?, ? extends Serializable>>) Class.forName(TestNoPersistentDao.class.getName()));
        daoBeanFactory.getObject();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNoDaoType() throws Exception {
        daoBeanFactory.setDao((Class<? extends Dao<?, ? extends Serializable>>) Class.forName(BigDecimal.class.getName()));
        daoBeanFactory.getObject();
    }

    @Test
    public void testTargetDetect() throws Exception {
        final Long id = 1l;
        this.context.checking(new Expectations() {
            {
                allowing(DaoBeanFactoryTest.this.sessionFactory)
                        .getCurrentSession();
                will(Expectations.returnValue(DaoBeanFactoryTest.this.session));
                one(DaoBeanFactoryTest.this.session).load(BigDecimal.class, id);
                will(Expectations.returnValue(BigDecimal.ONE));
            }
        });
        @SuppressWarnings("unchecked")
        final Dao<BigDecimal, Long> dao = (Dao<BigDecimal, Long>) this.daoBeanFactory
                .getObject();
        Assert.assertTrue(BigDecimal.ONE == dao.load(id));
    }

    @Test
    void testBaseBeanFactory() {

        this.daoBeanFactory.setPersistent(BigDecimal.class);

        Assert.assertTrue(this.daoBeanFactory.getDao() == TestDao.class);
        Assert.assertTrue(this.daoBeanFactory.isSingleton() == false);
        Assert.assertTrue(this.daoBeanFactory.getSessionFactory() == this.sessionFactory);
        this.daoBeanFactory.setSingleton(true);
        Assert.assertTrue(this.daoBeanFactory.isSingleton());
        Assert.assertTrue(this.daoBeanFactory.getObjectType() == TestDao.class);
    }

    @Test
    public void testCreate() throws Exception {
        this.daoBeanFactory.setPersistent(BigDecimal.class);
        this.context.checking(new Expectations() {
            {
                allowing(DaoBeanFactoryTest.this.sessionFactory)
                        .getCurrentSession();
                will(Expectations.returnValue(DaoBeanFactoryTest.this.session));
                one(DaoBeanFactoryTest.this.session).save(BigDecimal.ONE);
            }
        });

        final TestDao dao = (TestDao) this.daoBeanFactory.getObject();
        dao.create(BigDecimal.ONE);
        this.context.assertIsSatisfied();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindByName() throws Exception {
        this.daoBeanFactory.setPersistent(BigDecimal.class);
        final org.hibernate.Query q = this.context
                .mock(org.hibernate.Query.class);
        final Iterator it = this.context.mock(Iterator.class);
        this.context.checking(new Expectations() {
            {
                allowing(DaoBeanFactoryTest.this.sessionFactory)
                        .getCurrentSession();
                will(Expectations.returnValue(DaoBeanFactoryTest.this.session));
                one(DaoBeanFactoryTest.this.session).createQuery(
                        "select e from MyObject e where e.name like :name ");
                will(Expectations.returnValue(q));
                one(q).setParameter("name", "test");
                one(q).iterate();
                will(Expectations.returnValue(it));

            }
        });

        final TestDao dao = (TestDao) this.daoBeanFactory.getObject();
        final Iterator<BigDecimal> iterator = dao.findByNames("test");
        this.context.assertIsSatisfied();
        Assert.assertSame(iterator, it);
    }

    @Test
    public void testfindByCode() throws Exception {
        final int fetchSize = 10;
        final int offset = 3;
        this.daoBeanFactory.setPersistent(BigDecimal.class);
        final org.hibernate.Query q = this.context
                .mock(org.hibernate.Query.class);
        final List<BigDecimal> it = ImmutableList.of(BigDecimal.ONE,
                BigDecimal.TEN);
        this.context.checking(new Expectations() {
            {
                allowing(DaoBeanFactoryTest.this.sessionFactory)
                        .getCurrentSession();
                will(Expectations.returnValue(DaoBeanFactoryTest.this.session));
                one(DaoBeanFactoryTest.this.session).getNamedQuery(
                        "TestDao.findByCode");
                will(Expectations.returnValue(q));
                one(q).list();
                will(Expectations.returnValue(it));
                one(q).setFirstResult(offset);
                one(q).setFetchSize(fetchSize);

            }
        });

        final TestDao dao = (TestDao) this.daoBeanFactory.getObject();
        final List<BigDecimal> list = dao.findByCode("useless", fetchSize,
                offset);
        this.context.assertIsSatisfied();
        Assert.assertSame(list, it);
    }

    @Test
    public void testfindUniqueResult() throws Exception {
        this.daoBeanFactory.setPersistent(BigDecimal.class);
        final org.hibernate.Query q = this.context
                .mock(org.hibernate.Query.class);
        final BigDecimal result = new BigDecimal(71);
        this.context.checking(new Expectations() {
            {
                allowing(DaoBeanFactoryTest.this.sessionFactory)
                        .getCurrentSession();
                will(Expectations.returnValue(DaoBeanFactoryTest.this.session));
                one(DaoBeanFactoryTest.this.session).getNamedQuery(
                        "TestDao.findById");
                will(Expectations.returnValue(q));
                one(q).uniqueResult();
                will(Expectations.returnValue(result));

            }
        });

        final TestDao dao = (TestDao) this.daoBeanFactory.getObject();
        final BigDecimal r = dao.findById();
        this.context.assertIsSatisfied();
        Assert.assertEquals(r, result);
    }

    @Test
    public void testDelete() throws Exception {
        this.daoBeanFactory.setPersistent(BigDecimal.class);
        this.context.checking(new Expectations() {
            {
                allowing(DaoBeanFactoryTest.this.sessionFactory)
                        .getCurrentSession();
                will(Expectations.returnValue(DaoBeanFactoryTest.this.session));
                one(DaoBeanFactoryTest.this.session).delete(BigDecimal.ONE);
            }
        });

        final TestDao dao = (TestDao) this.daoBeanFactory.getObject();
        dao.delete(BigDecimal.ONE);
        this.context.assertIsSatisfied();
    }

    @Test
    public void testToString() throws Exception {

        final TestDao dao = (TestDao) this.daoBeanFactory.getObject();
        Assert.assertEquals(dao.toString(), TestDao.class.getName());
    }

}
