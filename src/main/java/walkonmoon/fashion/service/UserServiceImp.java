package walkonmoon.fashion.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import walkonmoon.fashion.dao.UserDAO;
import walkonmoon.fashion.model.User;

import java.util.List;
@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Transactional
    @Override
    public List<User> getAll() {
        return userDAO.getAll();
    }
    @Transactional
    @Override
    public User getById(int id) {
        return null;
    }
    @Transactional
    @Override
    public void add(User user) {

    }
    @Transactional
    @Override
    public void delete(int id) {

    }
}
