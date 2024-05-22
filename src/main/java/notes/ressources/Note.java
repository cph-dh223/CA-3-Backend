package notes.ressources;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

}
