package notes.controlers;


import java.util.stream.Collectors;
import java.text.ParseException;


import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import notes.daos.NoteDAO;
import notes.dtos.NoteDTO;
import notes.dtos.UserDTO;
import notes.exceptions.ApiException;
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
            var userID = getUserIdFromToken(ctx);
            var usersNotes = noteDAO.getAll(userID);
            // TODO error handeling
            ctx.json(om.writeValueAsString(usersNotes));
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            var id = Integer.parseInt(ctx.pathParam("id"));
            var userID = getUserIdFromToken(ctx);
            var note = noteDAO.getById(id);
            if (note.hasUser(userID)) {
                var noteDTO = new NoteDTO(note);
                ctx.status(HttpStatus.OK).json(om.writeValueAsString(noteDTO));
                return;
            }
            throw new ApiException(HttpStatus.UNAUTHORIZED.getCode(), "Unauthorized. Token not valid for note");
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            Note newNote = ctx.bodyAsClass(Note.class);
            newNote = noteDAO.create(newNote);
            String json = om.writeValueAsString(new NoteDTO(newNote));
            ctx.status(HttpStatus.CREATED).json(json);


        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            var title = Integer.parseInt(ctx.pathParam("id"));
            Note note = noteDAO.getById(title);
            noteDAO.delete(note);
            ctx.status(HttpStatus.NO_CONTENT);
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            var noteID = Integer.parseInt(ctx.pathParam("id"));
            var changedNote = ctx.bodyAsClass(NoteDTO.class);
            Note note = noteDAO.getById(noteID);

            note.setContent(changedNote.getContent());
            // TODO set up more of the changes

            noteDAO.update(note);

            String json = om.writeValueAsString(new NoteDTO(note));
            ctx.status(HttpStatus.OK).json(json);
        };
    }

    public Handler getByTitle() {
        return ctx -> {
            String title = ctx.pathParam("title");
            String userID = getUserIdFromToken(ctx);
            var notes = noteDAO.getAll(userID);
            var filterdNotes = notes.stream().filter(n -> n.getTitle().contains(title)).collect(Collectors.toList());
            ctx.status(HttpStatus.OK).json(om.writeValueAsString(filterdNotes.stream().map(n -> new NoteDTO(n)).collect(Collectors.toList())));
        };
    }

    public Handler sortByTitle() {
        return ctx -> {
            String title = ctx.pathParam("title");
            String userID = getUserIdFromToken(ctx);
            var notes = noteDAO.getAll(userID);
            notes.sort((a,b) -> a.getTitle().compareTo(b.getTitle()));
            ctx.status(HttpStatus.OK).json(om.writeValueAsString(notes.stream().map(n -> new NoteDTO(n)).collect(Collectors.toList())));
        };
    }

    public Handler sortByCategory() {
        return ctx -> {
            String title = ctx.pathParam("title");
            String userID = getUserIdFromToken(ctx);
            var notes = noteDAO.getAll(userID);
            notes.sort((a,b) -> a.getCategory().compareTo(b.getCategory()));
            ctx.status(HttpStatus.OK).json(om.writeValueAsString(notes.stream().map(n -> new NoteDTO(n)).collect(Collectors.toList())));
        };
    }

    public Handler sortByDate() {
        return ctx -> {
            String title = ctx.pathParam("title");
            String userID = getUserIdFromToken(ctx);
            var notes = noteDAO.getAll(userID);
            notes.sort((a,b) -> a.getDate().compareTo(b.getDate()));
            ctx.status(HttpStatus.OK).json(om.writeValueAsString(notes.stream().map(n -> new NoteDTO(n)).collect(Collectors.toList())));
        };
    }
  
    private String getUserIdFromToken(Context ctx) throws ParseException {
        var header = ctx.headerMap();
        var token = (header.get("Authorization").split(" "))[1];
        var userID = TokenUtils.getUserIdFromToken(token);
        return userID;
    }
}
