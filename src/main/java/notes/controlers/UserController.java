package notes.controlers;

import io.javalin.http.Handler;
import notes.daos.UserDAO;

public class UserController implements IController {
  private UserDAO userDAO;

  public UserController(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  @Override
  public Handler getAll() {
    return ctx -> {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    };
  }

  @Override
  public Handler getById() {
    return ctx -> {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getById'");
    };
  }

  @Override
  public Handler create() {
    return ctx -> {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'create'");
    };}

  @Override
  public Handler delete() {
    return ctx -> {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'delete'");
    };}

  @Override
  public Handler update() {
    return ctx -> {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'update'");
    };
  }
}
