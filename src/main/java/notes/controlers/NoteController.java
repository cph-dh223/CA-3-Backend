package notes.controlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.http.Handler;
import notes.daos.NoteDAO;
import notes.utils.TokenUtils;

public class NoteController implements IController {

    NoteDAO noteDAO;
    ObjectMapper om;
    public NoteController(NoteDAO noteDAO){
        this.noteDAO = noteDAO;
        om = new ObjectMapper();
    }

    /**
     * Fetches all notes from a spesific user
     */
    @Override
    public Handler getAll() {
        return ctx -> {
            var header = ctx.headerMap();
            var token = (header.get("Authorization").split(" "))[1];
            var userDTO = TokenUtils.getUserWithRolesFromToken(token);
            var usersNotes = noteDAO.getAll(userDTO.getEmail());
        };
    }

    @Override
    public Handler getById() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public Handler create() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public Handler delete() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Handler update() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
    
}
