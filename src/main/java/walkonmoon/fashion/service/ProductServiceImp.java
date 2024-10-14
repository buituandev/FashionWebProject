package walkonmoon.fashion.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import walkonmoon.fashion.dao.ProductDAO;
import walkonmoon.fashion.model.Product;
import java.util.List;
@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductDAO productDAO;
    @Transactional
    @Override
    public List<Product> getAll() {
        return productDAO.getAll();
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