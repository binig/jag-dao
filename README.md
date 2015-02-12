[![Build Status](https://travis-ci.org/binig/jag-dao.svg?branch=master)](https://travis-ci.org/binig/jag-dao)
[![Coverage Status](https://img.shields.io/coveralls/binig/jag-dao.svg)](https://coveralls.io/r/binig/jag-dao)

Jag-DAO
===
Simple implementation of the Generic DAO pattern based on Interfaces annotation Spring and Hibernate


add this dependency to your project


```
<dependency>
  <groupId>org.bin2</groupId>
  <artifactId>jag-dao</artifactId>
  <version>0.13</version>
  <scope>compile</scope>
</dependency>
```
define your DAO interface

```
public interface MyDaoInterface extends Dao<MyObject, Long>{
 @Query("from MyObject where date > :date")
 List<MyObject> findAllByDate( @NamedParameter("date") Date date);
}
```
define the implementation of your dao in spring

```
<bean id="cartDao" class="org.bin2.jag.dao.DaoBeanFactory">
    <property name="dao" value="xxx.yyy.MyDaoInterface"/>
</bean>
```
and use it

```
public class MyClassDemo {
   @Resource
   private MyDaoInterface cartDao;


   @Transactional
   public void process(Date date) {
   ...
   List<MyObject> object = cartDao.findAllByDate(date);
   ....
   }
}
```
