package caching;



//second level caching is by default disabled by hibernate.
//Hibernate only needs to be provided with an implementation of the org.hibernate.cache.spi.RegionFactory interface in order to
//enable the second level cache.
//we have added EhCache in the maven dependency and hibernate.cfg.
//we also added the @Cacheable annotation to our entity
//Entities are not stored in second-level cache as Java instances, but rather in their disassembled (hydrated) state:

//-Id (primary key) is not stored (it is stored as part of the cache key)
//-Transient properties are not stored
//-Collections are not stored (see below for more details)
//-Non-association property values are stored in their original form
//-Only id (foreign key) is stored for ToOne associations

//Also,we have to explicitly indicate that collection (oneToMany or ManyToMany) relations are cacheable otherwise they're not cached.s
//Actually, Hibernate stores collections in separate cache regions, one for each collection. The region name is a fully qualified class name plus the name of collection property.


//session factory object is responsible for handling second level caching in hibernate therefore it is accessed through entire application
//not just only one single session.
//when the session factory is closed all cache within that session factory is cleared.


//let's demonstrate the concept with an example


import caching.entities.Student;
import caching.utils.HibernateSingleton;
import org.hibernate.SessionFactory;

public class SecondLevelCache {

    private static SessionFactory sessionFactory=HibernateSingleton.getInstance();





    public static void add(Student st) {
        try (var session = sessionFactory.openSession()) {
            var trx = session.beginTransaction();

            try {
                session.save(st);
                trx.commit();
                session.close();
            } catch (RuntimeException e) {
                trx.rollback();
            }
        }
    }







    public static void main(String[] args) {





        //first we add some sample to the database
        Student student=new Student(2,"firstName","LastName","1234567890");
        add(student);

        //at this stage the student entity is available in the second level cache as well as
        //the first level cache.



        var session1=sessionFactory.openSession();
        Student student1=session1.load(Student.class,student.getId());

        System.out.println(student1);
        session1.close();



        //now we create another session with fetches the student class not from the database but from the L2 cache
        var session2=sessionFactory.openSession();
        Student student2=session2.load(Student.class,student.getId());
        System.out.println(student2);
        session2.close();

    }
}
