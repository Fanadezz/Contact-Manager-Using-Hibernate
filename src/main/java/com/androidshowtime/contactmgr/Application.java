package com.androidshowtime.contactmgr;

import com.androidshowtime.contactmgr.model.Contact;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

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

        Contact contact = new Contact.
                ContactBuilder("Tonnie", "Vicenzio").withEmail("vontonnie@gmail.com")
                                                    .withPhone(723445813L)
                                                    .build();

        int id = save(contact);
//Display list of contacts before the update
        System.out.println("%n%nBefore update %n%n");
        fetchAllContacts().stream()
                          .forEach(System.out::println);

        //Get the persisted contact
        Contact c = findContactById(id);
        //Update the contact

        c.setFirstName("Joseph");
        c.setLastName("Maina");
        //Persist the changes
        System.out.println("%n%nUpdating ..... %n%n");
        update(c);
        System.out.println("%n%nUpdate Complete! %n%n");
        //Display a list of contacts after the update
        System.out.println("%n%nAfter update %n%n");
        fetchAllContacts().stream().forEach(System.out::println);

        //Delete the last contact
        System.out.println("%n%nDeleting contact %n%n");
        delete(c);

//Display Contacts
        System.out.println("%n%nDisplaying contacts after Deletion");
        fetchAllContacts().stream().forEach(System.out::println);
    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts() {

        //Open a session

        Session session = sessionFactory.openSession();

        // UPDATED: Create CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();
        // UPDATED: Create CriteriaQuery
        CriteriaQuery<Contact> criteria = builder.createQuery(Contact.class);

        // UPDATED: Specify criteria root
        criteria.from(Contact.class);

        // UPDATED: Execute query
        List<Contact> contacts = session.createQuery(criteria)
                                        .getResultList();

        // Close the session

        session.close();

        return contacts;
    }

    private static Contact findContactById(int id) {

        //Open Session
        Session session = sessionFactory.openSession();
//Retrieve the persistent object (or null if not found)
        Contact contact = session.get(Contact.class, id);

        //Close the session
        session.close();
        return contact;
    }

    private static int save(Contact contact) {
        //Open a session
        Session session = sessionFactory.openSession();

        //Begin a transaction
        session.beginTransaction();

        // Use the session to save the contact
        int id = (int) session.save(contact);
        //Commit the transaction
        session.getTransaction()
               .commit();
        // Close the session
        session.close();
        return  id;
    }

    private static void update(Contact contact) {
        //Open a session
        Session session = sessionFactory.openSession();
        //Begin a transaction

        session.beginTransaction();
        //Use the session to update the contact
        session.update(contact);
        //Commit the transaction

        session.getTransaction()
               .commit();
        //Close the session
        session.close();


    }

    private static void delete (Contact contact){

        //Open a session
        Session session = sessionFactory.openSession();
        //Begin a Transaction
        session.beginTransaction();
        //use session to delete contact
        session.delete(contact);
        //Commit the transaction
        session.getTransaction().commit();
        //close session
        session.close();



    }

}



