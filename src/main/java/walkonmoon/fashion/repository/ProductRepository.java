package walkonmoon.fashion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import walkonmoon.fashion.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategoryId(int categoryId);
    List<Product> findByIdIn(List<Integer> productIds);
}
