package notes.daos;

import notes.exceptions.EntityNotFoundException;
import notes.ressources.Role;
import notes.ressources.User;

public interface ISecurityDAO {
    User createUser(String username, String password);
    Role createRole(String role);
    User addRoleToUser(String username, String role);
    User verifyUser(String username, String password) throws EntityNotFoundException;
}
