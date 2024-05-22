package notes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import notes.daos.UserDAO;
import notes.ressources.Category;
import notes.ressources.Note;
import notes.ressources.Role;
import notes.ressources.User;

public class TestUtils {
    public void createNotesAndUsers(EntityManagerFactory emfTest) {
        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Note n").executeUpdate();

            Note n1 = new Note("title1", "this is the first note", Category.NOTE);
            Note n2 = new Note("title2", "this is the second note", Category.REMINDER);
            Note n3 = new Note("title3", "this is the third note", Category.NOTE);
            Note n4 = new Note("title4", "this is the fourth note", Category.REMINDER);

            em.persist(n1);
            em.persist(n2);
            em.persist(n3);
            em.persist(n4);
            em.getTransaction().commit();

            // Setup notes
            em.getTransaction().begin();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            em.getTransaction().commit();

            // Insert data into the database
            User u1 = new User("user", "user");
            User u2 = new User("admin", "admin");

            Role r1 = new Role("admin");
            Role r2 = new Role("user");

            u2.addRole(r1);
            u1.addRole(r2);
            u1.addNote(n1);
            u1.addNote(n2);

            em.getTransaction().begin();
            em.persist(r1);
            em.persist(r2);
            em.persist(u1);
            em.persist(u2);
            em.getTransaction().commit();
        }
    }

}
