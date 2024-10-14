package walkonmoon.fashion.dao;

import walkonmoon.fashion.model.Product;
import walkonmoon.fashion.model.User;

import java.util.List;

public interface ProductDAO {
    List<Product> getAll();
    Product getById(int id);
    void add(Product product);
    void delete(int id);
}
