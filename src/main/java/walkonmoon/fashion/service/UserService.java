package walkonmoon.fashion.service;

import walkonmoon.fashion.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getById(int id);
    void add(User user);
    void delete(int id);
}
