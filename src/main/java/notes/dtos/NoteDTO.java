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

    public NoteDTO(Note note){
        title = note.getTitle();
        content = note.getContent();
    }
}
