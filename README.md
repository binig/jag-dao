Jag-DAO
===
Simple implementation of the Generic DAO pattern based on Interfaces annotation Spring and Hibernate

can be found on the repo http://bin2.org/archiva/repository/publicRelease/ and the maven site is available here http://jag-dao.bin2.org/

add this dependency to your project


```
<dependency>
  <groupId>org.bin2</groupId>
  <artifactId>jag-dao</artifactId>
  <version>0.10</version>
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
