package notes.config;


import com.fasterxml.jackson.databind.ObjectMapper;

import notes.controlers.ISecurityController;
import notes.controlers.SecurityController;
import notes.daos.UserDAO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;
import io.javalin.security.RouteRole;


import static io.javalin.apibuilder.ApiBuilder.*;



public class Routs {
    private static ObjectMapper om = new ObjectMapper();
    
    public static EndpointGroup getSecurityRoutes(EntityManagerFactory emf) {
        ISecurityController securityController = new SecurityController(new UserDAO(emf));
        return ()->{
            path("/auth", ()->{
                post("/login", securityController.login(),Role.ANYONE);
                post("/register", securityController.register(),Role.ANYONE);
            });
        };
    }
    public static EndpointGroup securedRoutes(EntityManagerFactory emf){
        ISecurityController securityController = new SecurityController(new UserDAO(emf));
        return ()->{
            path("/protected", ()->{
                before(securityController.authenticate());
                get("/user_demo",(ctx)->ctx.json(om.createObjectNode().put("msg",  "Hello from USER Protected")),Role.USER);
                get("/admin_demo",(ctx)->ctx.json(om.createObjectNode().put("msg",  "Hello from ADMIN Protected")),Role.ADMIN);
            });
        };
    }
    
    public static EndpointGroup unsecuredRoutes(EntityManagerFactory emf){
        return  () -> {
            get("/",ctx -> ctx.result(new ObjectMapper().writeValueAsString("Hello World")), Role.ANYONE);
            
        };
    }
    
    public enum Role implements RouteRole {
        ANYONE, 
        USER, 
        ADMIN 
    }
}
