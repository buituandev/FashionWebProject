package walkonmoon.fashion.repository;

import org.springframework.data.repository.CrudRepository;
import walkonmoon.fashion.model.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
