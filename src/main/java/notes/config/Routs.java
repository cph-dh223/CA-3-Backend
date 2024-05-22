package notes.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import notes.controlers.IController;
import notes.controlers.ISecurityController;
import notes.controlers.NoteController;
import notes.controlers.SecurityController;
import notes.controlers.UserController;
import notes.daos.NoteDAO;
import notes.daos.UserDAO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;
import io.javalin.security.RouteRole;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routs {
    private static ObjectMapper om = new ObjectMapper();

    public static EndpointGroup getSecurityRoutes(EntityManagerFactory emf) {
        ISecurityController securityController = new SecurityController(new UserDAO(emf));
        return () -> {
            path("/auth", () -> {
                post("/login", securityController.login(), Role.ANYONE);
                post("/register", securityController.register(), Role.ANYONE);
            });
        };
    }

    public static EndpointGroup securedRoutes(EntityManagerFactory emf) {
        ISecurityController securityController = new SecurityController(new UserDAO(emf));
        NoteController noteController = new NoteController(new NoteDAO(emf));
        UserController userController = new UserController(new UserDAO(emf));

        return () -> {
            path("/protected", () -> {
                before(securityController.authenticate());
                get("/user_demo", (ctx) -> ctx.json(om.createObjectNode().put("msg", "Hello from USER Protected")),
                        Role.USER);
                get("/admin_demo", (ctx) -> ctx.json(om.createObjectNode().put("msg", "Hello from ADMIN Protected")),
                        Role.ADMIN);
            });
            path("/user", () -> {
                path("/note", () -> {
                    post("/create", noteController.create(), Role.USER, Role.ADMIN);
                    get("/search/{id}", noteController.getById(), Role.USER, Role.ADMIN);
                    put("/update/{id}", noteController.update(), Role.USER, Role.ADMIN);
                    delete("/delete/{id}", noteController.delete(), Role.USER, Role.ADMIN);
                });
            });
            path("/notes", () -> {
                get("/", noteController.getAll(), Role.USER, Role.ADMIN);
                get("/search/{title}", noteController.getByTitle(), Role.ADMIN, Role.USER);
                get("/sort/title", noteController.sortByTitle(), Role.ADMIN, Role.USER);
                get("/sort/date", noteController.sortByDate(), Role.ADMIN, Role.USER);
                get("/sort/category", noteController.sortByCategory(), Role.ADMIN, Role.USER);
            });
            path("/users", () -> {
                get("/", userController.getAll(), Role.ADMIN);
                get("/{id}", userController.getById(), Role.ADMIN);
                put("/update", userController.update(), Role.ADMIN);
                delete("/delete/{id}", userController.delete(), Role.ADMIN);
            });

        };

    }

    public static EndpointGroup unsecuredRoutes(EntityManagerFactory emf) {
        return () -> {
            get("/", ctx -> ctx.result(new ObjectMapper().writeValueAsString("Hello World")), Role.ANYONE);

        };
    }

    public enum Role implements RouteRole {
        ANYONE,
        USER,
        ADMIN
    }
}
