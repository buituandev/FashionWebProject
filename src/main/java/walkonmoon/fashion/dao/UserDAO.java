package walkonmoon.fashion.dao;

import walkonmoon.fashion.model.User;

import java.util.List;

public interface UserDAO {
    List<User> getAll();
    User getById(int id);
    void add(User user);
    void delete(int id);

}
