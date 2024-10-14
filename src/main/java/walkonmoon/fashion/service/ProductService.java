package walkonmoon.fashion.service;

import walkonmoon.fashion.dao.ProductDAO;
import walkonmoon.fashion.model.Product;
import walkonmoon.fashion.model.User;

import java.util.List;

public interface ProductService {
    List<Product> getAll();
    Product getById(int id);
    void add(Product product);
    void delete(int id);
}
