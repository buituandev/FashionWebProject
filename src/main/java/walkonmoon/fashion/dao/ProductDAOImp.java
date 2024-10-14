package walkonmoon.fashion.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.Product;
import walkonmoon.fashion.model.User;

import java.util.List;
@Service
public class ProductDAOImp implements ProductDAO {
    @Autowired
    private EntityManager em;
    @Transactional
    @Override
    public List<Product> getAll() {
        Session session = em.unwrap(Session.class);
        Query query = session.createQuery("from Product ");
        List<Product> products = query.getResultList();
        return products;
    }
    @Transactional
    @Override
    public Product getById(int id) {
        return null;
    }
    @Transactional
    @Override
    public void add(Product product) {

    }
    @Transactional
    @Override
    public void delete(int id) {

    }
}
