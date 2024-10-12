package walkonmoon.fashion.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.User;

import java.util.List;
@Service
public class UserDAOImp implements UserDAO {
    @Autowired
    private EntityManager em;
    @Transactional
    @Override
    public List<User> getAll() {
        Session session = em.unwrap(Session.class);
        Query query = session.createQuery("from User");
        List<User> userList = query.getResultList();
        return userList;
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
