package notes.dtos;

import java.util.Set;

import notes.ressources.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private String email;
    private String password;
    private Set<String> roles;

    public UserDTO (User user){
        email = user.getEmail();
        password = user.getPassword();
        roles    = user.getRolesAsStrings();
    }

    public UserDTO(String username, Set<String> rolesSet) {
        this.email = username;
        roles = rolesSet;
    }
}
