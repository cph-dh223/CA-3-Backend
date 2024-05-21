package notes.ressources;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Note {
    private String title;
    private int id;
    private Category category;
    private LocalDate date;
    private LocalDate lastEditDate;

    @ManyToMany
    private Set<User> users;
    private String content;

    public Note(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void addUser(User user){
        users.add(user);
    }
}
