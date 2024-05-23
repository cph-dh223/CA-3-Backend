package notes;

import notes.config.ApplicationConfig;
import notes.config.HibernateConfig;
import notes.config.Routs;
import notes.ressources.Note;
import notes.ressources.Role;
import notes.ressources.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class main {
    public static void main(String[] args) {
        startServer(7070);
    }

    public static void startServer(int port) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        ApplicationConfig applicationConfig = ApplicationConfig.getInstance(emf);
        setup(emf);
        applicationConfig
                .initiateServer()
                .startServer(port)
                .setExceptionHandling()
                .checkSecurityRoles()
                .configureCors()
                .setRoute(Routs.unsecuredRoutes(emf))
                .setRoute(Routs.getSecurityRoutes(emf))
                .setRoute(Routs.securedRoutes(emf))
            ;
    }
    

    private static void setup(EntityManagerFactory emf){
        try (EntityManager em = emf.createEntityManager()) {
            // if(em.find(Note.class, 1) != null) return;
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Note h").executeUpdate();
            em.createQuery("DELETE FROM User u").executeUpdate();
            em.createQuery("DELETE FROM Role r").executeUpdate();
            
            Note n1 = new Note("n1", "THIS IS NOTE 1");
            Note n2 = new Note("n2", "THIS IS NOTE 2");
            Note n3 = new Note("n3", "THIS IS NOTE 3");
            
            

            User admin = new User("admin", "1234"); //TODO bedere sequrety, admin credentails shuld not be this visible
            User u1 = new User("u1", "1234");
            User u2 = new User("u2", "1234");
            User u3 = new User("u3", "1234");
            User u4 = new User("u4", "1234");
        

            admin.addNote(n1);
            admin.addNote(n2);
            admin.addNote(n3);
            System.out.println(admin.getNotes());

            Role adminRole = new Role("admin");
            Role userRole = new Role("user");
            admin.addRole(adminRole);
            u1.addRole(userRole);
            u2.addRole(userRole);
            u3.addRole(userRole);
            u4.addRole(userRole);
            
            
            em.persist(n1);
            em.persist(n2);
            em.persist(n3);
            em.persist(admin);
            em.persist(adminRole);
            em.persist(userRole);
            em.persist(u1);
            em.persist(u2);
            em.persist(u3);
            em.persist(u4);

            em.getTransaction().commit();
        }
    }

}
