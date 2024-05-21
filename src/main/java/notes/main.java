package notes;

import notes.config.ApplicationConfig;
import notes.config.HibernateConfig;
import notes.config.Routs;
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
        // setup(emf);
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
    

    // private static void setup(EntityManagerFactory emf){
    //     try (EntityManager em = emf.createEntityManager()) {
    //         if(em.find(notes.class, 1) != null) return;
    //         em.getTransaction().begin();
    //         em.createQuery("DELETE FROM Room r").executeUpdate();
    //         em.createQuery("DELETE FROM notes h").executeUpdate();
    //         em.createQuery("DELETE FROM User u").executeUpdate();
    //         em.createQuery("DELETE FROM Role r").executeUpdate();
            
    //         notes h1 = new notes("h1", "Street 1");
    //         notes h2 = new notes("h2", "Street 2");
    //         notes h3 = new notes("h3", "Street 3");
    //         Room r1_1 = new Room(1, 100f);
    //         Room r1_2 = new Room(2, 1000f);
    //         Room r2_1 = new Room(1, 100f);
    //         Room r2_2 = new Room(2, 1000f);
    //         Room r3_1 = new Room(1, 100f);
    //         Room r3_2 = new Room(2, 1000f);

    //         h1.addRoom(r1_1);
    //         h1.addRoom(r1_2);
    //         h2.addRoom(r2_1);
    //         h2.addRoom(r2_2);
    //         h3.addRoom(r3_1);
    //         h3.addRoom(r3_2);
                
    //         em.persist(h1);
    //         em.persist(h2);
    //         em.persist(h3);
    //         em.persist(r1_1);
    //         em.persist(r1_2);
    //         em.persist(r2_1);
    //         em.persist(r2_2);
    //         em.persist(r3_1);
    //         em.persist(r3_2);

    //         User admin = new User("admin", "1234");
    //         Role adminRole = new Role("admin");
    //         admin.addRole(adminRole);

    //         em.persist(admin);
    //         em.persist(adminRole);

    //         em.getTransaction().commit();
    //     }
    // }

}
