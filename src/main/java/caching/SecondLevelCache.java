package caching;



//second level caching is by default disabled by hibernate.
//Hibernate only needs to be provided with an implementation of the org.hibernate.cache.spi.RegionFactory interface in order to
//enable the second level cache.
//we have added EhCache in the maven dependency and hibernate.cfg.
//we also added the @Cacheable annotation to our entity

//session factory object is responsible for handling second level caching in hibernate therefore it is accessed through entire application
//not just only one single session.
//when the session factory is closed all cache within that session factory is cleared.

//let's demonstrate the concept with an example


import caching.entities.Student;
import caching.utils.HibernateSingleton;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;

public class SecondLevelCache {

    private static SessionFactory sessionFactory=HibernateSingleton.getInstance();






    public static void add(Student st) {
        try (var session = sessionFactory.openSession()) {
            var trx = session.beginTransaction();

            try {
                session.save(st);
                session.close();
                trx.commit();
            } catch (RuntimeException e) {
                trx.rollback();
            }
        }
    }







    public static void main(String[] args) {





        //first we add some sample to the database
        Student student=new Student(null,"firstName","LastName","1234567890");
        add(student);

        //at this stage the student entity is available in the second level cache as well as
        //the first level cache.




    }
}
