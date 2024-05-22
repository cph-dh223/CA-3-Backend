package notes.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import notes.config.HibernateConfig;
import notes.exceptions.EntityNotFoundException;
import notes.ressources.Role;
import notes.ressources.User;

import java.util.Collection;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Purpose:
 *
 * @author: Thomas Hartmann
 */
public class UserDAO implements ISecurityDAO {

    private EntityManagerFactory emf;

    public UserDAO(EntityManagerFactory _emf) {
        this.emf = _emf;
    }

    @Override
    public User createUser(String email, String password) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = new User(email, password);
            Role userRole = em.find(Role.class, "user");
            if (userRole == null) {
                userRole = new Role("user");
                em.persist(userRole);
            }
            user.addRole(userRole);
            em.persist(user);
            em.getTransaction().commit();
            return user;
        }
    }

    @Override
    public User verifyUser(String email, String password) throws EntityNotFoundException {
        try (EntityManager em = emf.createEntityManager()) {
            User user = em.find(User.class, email);
            if (user == null)
                throw new EntityNotFoundException("No user found with email: " + email);
            if (!user.verifyUser(password))
                throw new EntityNotFoundException("Wrong password");
            return user;
        }
    }

    @Override
    public Role createRole(String role) {
        // return null;

        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public User addRoleToUser(String email, String role) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = em.find(User.class, email);
            Role userRole = em.find(Role.class, role);
            if (userRole == null) {
                userRole = new Role(role);
                em.persist(userRole);
            }
            user.addRole(userRole);
            em.persist(user);
            em.getTransaction().commit();
            return user;
        }
    }

    public List<User> getAllUsers() {
        try (var em = emf.createEntityManager()) {
            TypedQuery<User> q = em.createQuery("SELECT u FROM User u", User.class);
            List<User> users = q.getResultList();
            return users;
        }
    }

    public User getUserById(String id) {
        try (var em = emf.createEntityManager()) {
            return em.find(User.class, id);
        }
    }

    public List<Role> getAllRoles() {
        try (var em = emf.createEntityManager()) {
            TypedQuery<Role> q = em.createQuery("SELECT r FROM Role r", Role.class);
            List<Role> roles = q.getResultList();
            return roles;
        }
    }

    public void deleteUser(String id) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            var userToRemove = em.find(User.class, id);
            em.remove(userToRemove);
            em.getTransaction().commit();

        }
    }

    public User updateUser(User user) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            user.getRoles().stream().forEach(role -> {
                Role userRole = em.find(Role.class, role.getName());
                if (userRole == null) {
                    userRole = new Role(role.getName());
                    em.persist(userRole);
                }
                role = userRole;
            });
            em.merge(user);
            em.getTransaction().commit();
        }
        return user;
    }
}
