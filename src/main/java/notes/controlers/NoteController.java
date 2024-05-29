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
import notes.ressources.Category;
import notes.ressources.Note;
import notes.ressources.User;
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
            ctx.json(
                    om.writeValueAsString((usersNotes.stream().map(n -> new NoteDTO(n)).collect(Collectors.toList()))));
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
            NoteDTO newNoteDTO = ctx.bodyAsClass(NoteDTO.class);
            Note newNote = new Note(newNoteDTO.getTitle(), newNoteDTO.getContent(),
                    Category.valueOf(newNoteDTO.getCategory()));
            newNote.addUser(new User(getUserIdFromToken(ctx), null));
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
            System.out.println("note:" + note.toString());

            note.setTitle(changedNote.getTitle());
            note.setContent(changedNote.getContent());
            note.setCategory(changedNote.getCategory());
            if(changedNote.getColaborators() != null){
                note.setUsers(changedNote.getColaborators());
            }


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
            ctx.status(HttpStatus.OK).json(
                    om.writeValueAsString(filterdNotes.stream().map(n -> new NoteDTO(n)).collect(Collectors.toList())));
        };
    }

    public Handler sortByTitle() {
        return ctx -> {
            String userID = getUserIdFromToken(ctx);
            var notes = noteDAO.getAll(userID);
            notes.sort((a, b) -> a.getTitle().compareTo(b.getTitle()));
            ctx.status(HttpStatus.OK)
                    .json(om.writeValueAsString(notes.stream().map(n -> new NoteDTO(n)).collect(Collectors.toList())));
        };
    }

    public Handler sortByCategory() {
        return ctx -> {
            String userID = getUserIdFromToken(ctx);
            var notes = noteDAO.getAll(userID);
            notes.sort((a, b) -> a.getCategory().compareTo(b.getCategory()));
            ctx.status(HttpStatus.OK)
                    .json(om.writeValueAsString(notes.stream().map(n -> new NoteDTO(n)).collect(Collectors.toList())));
        };
    }

    public Handler sortByDate() {
        return ctx -> {
            String userID = getUserIdFromToken(ctx);
            var notes = noteDAO.getAll(userID);
            notes.sort((a, b) -> a.getDate().compareTo(b.getDate()));
            ctx.status(HttpStatus.OK)
                    .json(om.writeValueAsString(notes.stream().map(n -> new NoteDTO(n)).collect(Collectors.toList())));
        };
    }

    public Handler findByTitle() {
        return ctx -> {
            String seachTitle = ctx.pathParam("title");
            String userID = getUserIdFromToken(ctx);
            var notes = noteDAO.getAll(userID);
            ctx.status(HttpStatus.OK)
                    .json(om.writeValueAsString(notes.stream()
                            .filter(n -> n.getTitle().contains(seachTitle))
                            .map(n -> new NoteDTO(n))
                            .collect(Collectors.toList())));
        };

    }

    private String getUserIdFromToken(Context ctx) throws ParseException {
        var header = ctx.headerMap();
        var token = (header.get("Authorization").split(" "))[1];
        var userID = TokenUtils.getUserIdFromToken(token);
        return userID;
    }
}
