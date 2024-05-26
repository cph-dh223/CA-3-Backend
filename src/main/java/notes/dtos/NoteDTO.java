package notes.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import notes.ressources.Note;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class NoteDTO {

    private int id;

    private String title;
    private String content;
    private String category;

    public NoteDTO(Note note){
        id = note.getId();
        title = note.getTitle();
        content = note.getContent();
        category = note.getCategory().toString();
    }
}
