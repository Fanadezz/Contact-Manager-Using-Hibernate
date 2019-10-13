package com.androidshowtime.contactmgr;

import com.androidshowtime.contactmgr.model.Contact;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Application {

    //Hold a reusable reference to a SessionFactory (since we need only one)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {

        //Create a StandardServiceRegistry
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure()
                                                                                     .build();
        return new MetadataSources(registry).buildMetadata()
                                            .buildSessionFactory();
    }

    public static void main(String[] args) {

        Contact contact = new Contact.ContactBuilder("Tonnie", "Vicenzio").withEmail("vontonnie@gmail.com")
                                                                          .withPhone(723445813L)
                                                                          .build();

        //Open a session
        Session session = sessionFactory.openSession();

        //Begin a transaction
        session.beginTransaction();

        // Use the session to save the contact
        session.save(contact);

        //Commit the transaction
        session.getTransaction()
               .commit();
        // Close the session
        session.close();
        ;
    }


}



