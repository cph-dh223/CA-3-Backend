package notes.ressources;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.metamodel.mapping.internal.GeneratedValuesProcessor;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private Category category;
    private LocalDate date;
    private LocalDate lastEditDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonBackReference
    private Set<User> users = new HashSet<>();
    private String content;

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public Note(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public Note(int id, String title, String content, Category category, LocalDate date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.date = date;
    }


    public void addUser(User user) {
        users.add(user);
    }

    @PrePersist
    public void prePersist() {
        date = LocalDate.now();
    }

    @PreUpdate
    public void preUpdate() {
        lastEditDate = LocalDate.now();
    }

    public boolean hasUser(String userID) {
        return users.stream().map(u -> u.getEmail().equals(userID)).reduce(false, (acc, u) -> acc || u ? true : false);
    }
    
    public void removeUser(User user) {
        users.remove(user);
    }

    @PreRemove
    public void removeUser() {
        users.forEach(u -> u.removeNote(this));
    }

    public void setCategory(String categoryName){
        category = Category.valueOf(categoryName);
    }

    public void setUsers(Set<String> usernames){
        Set<String> currentUsernames = users.parallelStream().map(u -> u.getEmail()).collect(Collectors.toSet());
        for (String username : usernames) {
            if(currentUsernames.contains(username)) continue; // jumps over users allready connected to the note
            users.add(new User(username));
        }
    }

}
