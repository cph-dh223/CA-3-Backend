package notes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import notes.ressources.Category;
import notes.ressources.Note;
import notes.ressources.Role;
import notes.ressources.User;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.put;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.mail.Address;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.apibuilder.EndpointGroup;

public class TestUtils {
    public void createnotessAndRooms(EntityManagerFactory emfTest) {
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
        }
    }

    public void createUsersAndRoles(EntityManagerFactory emfTest) {
        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM User u").executeUpdate();
            em.createQuery("DELETE FROM Role r").executeUpdate();

            User u1 = new User("admin", "admin");
            User u2 = new User("user", "user");

            Role r1 = new Role("admin");
            Role r2 = new Role("user");

            u1.addRole(r1);
            u2.addRole(r2);

            em.persist(u1);
            em.persist(u2);
            em.persist(r1);
            em.persist(r2);

            em.getTransaction().commit();
        }
    }

    public Map<String, User> getUsers(EntityManagerFactory emfTest) {
        return new UserDAO(emfTest).getAllUsers().stream().collect(Collectors.toMap(u -> u.getUsername(), u -> u));
    }

    public Map<String, Role> getRoles(EntityManagerFactory emfTest) {
        return new UserDAO(emfTest).getAllRoles().stream().collect(Collectors.toMap(r -> r.getName(), r -> r));
    }

}
