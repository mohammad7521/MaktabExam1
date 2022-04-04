package caching.utils;

import caching.entities.Student;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateSingleton {



    private static class LazyHolder{
        static SessionFactory INSTANCE;
        static {
            var registry = new StandardServiceRegistryBuilder()
                    .configure()
                    .build();

            INSTANCE = new MetadataSources(registry)
                    .addAnnotatedClass(Student.class)
                    .buildMetadata()
                    .buildSessionFactory();
        }

    }
    public static SessionFactory getInstance(){
        return LazyHolder.INSTANCE;
    }
}
