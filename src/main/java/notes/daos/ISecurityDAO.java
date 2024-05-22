package notes.daos;

import notes.exceptions.EntityNotFoundException;
import notes.ressources.Role;
import notes.ressources.User;

public interface ISecurityDAO {
    User createUser(String email, String password);
    Role createRole(String role);
    User addRoleToUser(String email, String role);
    User verifyUser(String email, String password) throws EntityNotFoundException;
}
