package notes.controlers;

import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import notes.daos.UserDAO;
import notes.dtos.UserDTO;

public class UserController implements IController {
    private UserDAO userDAO;
    private ObjectMapper om;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
        om = new ObjectMapper();
    }

    @Override
    public Handler getAll() {
        return ctx -> {
            ctx.status(HttpStatus.OK).json(om.writeValueAsString(
                    userDAO.getAllUsers().stream().map(u -> new UserDTO(u)).collect(Collectors.toList())));
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            var userId = ctx.pathParam("id");
            ctx.status(HttpStatus.OK).json(om.writeValueAsString(new UserDTO(userDAO.getUserById(userId))));
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            var userDTO = ctx.bodyAsClass(UserDTO.class);
            userDAO.createUser(userDTO.getEmail(), userDTO.getPassword());
            ctx.status(HttpStatus.CREATED);
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            var userId = ctx.pathParam("id");
            userDAO.deleteUser(userId);
            ctx.status(HttpStatus.NO_CONTENT);
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            var userDTO = ctx.bodyAsClass(UserDTO.class);
            var userToUpdate = userDAO.getUserById(userDTO.getEmail());
            userToUpdate.updateMailAndRolesFromDTO(userDTO);

            if (userDTO.getPassword() != null) {
                userToUpdate.updatePasswordFromDTO(userDTO);
            }

            userToUpdate = userDAO.updateUser(userToUpdate);
            ctx.status(HttpStatus.OK).json(om.writeValueAsString(new UserDTO(userToUpdate)));
        };
    }

    public Handler getAllEmails() {
        return ctx -> {
            ctx.status(HttpStatus.OK).json(om.writeValueAsString(
                    userDAO.getAllUsers().stream().map(u -> new UserDTO(u.getEmail(), u.getRoles().stream().map(r -> r.toString()).collect(Collectors.toSet()))).collect(Collectors.toList())));
        };
    }
}
