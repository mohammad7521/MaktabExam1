package caching;

import caching.entities.Student;
import org.hibernate.Session;
import caching.utils.HibernateSingleton;
import org.hibernate.SessionFactory;

public class FirstLevelCache {

    //Hibernate caching acts as a layer between the actual database and your application. I
    //There are mainly two types of caching:
    //First level cache
    //Second-level cache

    //the first level cache is enabled and second level cache is disabled by default in hibernate
    //in hibernate  The session object maintains the first-level cache.
    //an application can have as many session as necessary.
    //a data that is held by one session is not accessible to the entire application.

    //for example when we query an entity fo the first time that is retrieved from the database
    //it is stored in the first level cache.
    //now if we try to query the same object while the session is still open,it will be loaded from the cache not the database.


    //Demonstration:

    private static SessionFactory sessionFactory = HibernateSingleton.getInstance();


    //adding the student entity to the DB
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


    public static void showInfo(int id) {
        var session = sessionFactory.openSession();
        session.beginTransaction();


        //fetching the student entity from the DB for the first time which takes the fetched entity to the first level cache.
        Student loadedStudent = session.load(Student.class, id);
        System.out.println("loading the student from DB:");
        System.out.println(loadedStudent);


        //getting student attributes after loading it for the first time which is now the first level cache and does not need to be retrieved from the DB.
        System.out.println("calling the student attributes from the cache");
        System.out.println("id: "+ loadedStudent.getId());
        System.out.println("firstName: "+loadedStudent.getFirstName());
        System.out.println("lastName: "+loadedStudent.getLastName());
        System.out.println("national code: "+loadedStudent.getNationalCode());

        session.getTransaction().commit();
        session.close();
    }

    public static void main(String[] args) {

        Student student = new Student(1, "firstName", "lastName", "1234567890");

        add(student);
        showInfo(student.getId());
    }
}
