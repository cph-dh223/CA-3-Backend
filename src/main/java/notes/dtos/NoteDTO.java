package notes.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import notes.ressources.Note;

@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {
    private String title;
    private String content;

    public NoteDTO(Note note){
        title = note.getTitle();
        content = note.getContent();
    }
}
