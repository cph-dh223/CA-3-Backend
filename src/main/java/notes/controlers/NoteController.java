package notes.controlers;

import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import notes.daos.NoteDAO;
import notes.dtos.NoteDTO;
import notes.ressources.Note;
import notes.utils.TokenUtils;

public class NoteController implements IController {

    NoteDAO noteDAO;
    ObjectMapper om;

    public NoteController(NoteDAO noteDAO) {
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
            // TODO error handeling
            ctx.json(om.writeValueAsString(usersNotes));
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            var title = "n1"; // TODO get the rigth thing from the back end
            var note = noteDAO.getById(title);
            var noteDTO = new NoteDTO(note);
            ctx.json(om.writeValueAsString(noteDTO));
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            Note newNote = ctx.bodyAsClass(Note.class);
            newNote = noteDAO.create(newNote);
            String json = om.writeValueAsString(newNote);
            ctx.status(HttpStatus.CREATED).json(json);

        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            String title = ctx.pathParam("title");
            Note note = noteDAO.getById(title);
            noteDAO.delete(note);
            ctx.status(HttpStatus.NO_CONTENT);
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            String title = ctx.pathParam("title");
            Note changedNote = ctx.bodyAsClass(Note.class);
            Note note = noteDAO.getById(title);

            note.setContent(changedNote.getContent());
            // TODO set up more of the changes

            noteDAO.update(note);

            String json = om.writeValueAsString(note);
            ctx.status(HttpStatus.OK).json(json);
        };
    }

    public Handler getByTitle(){
        return ctx -> {
            String title = ctx.pathParam("title");
            String userID = getUserIdFromToken(ctx);
            var notes = noteDAO.getAll(userID);
            var filterdNotes = notes.stream().filter(n -> n.getTitle().contains(title)).collect(Collectors.toList());
            ctx.status(HttpStatus.OK).json(om.writeValueAsString(filterdNotes));
        };
    }

}
