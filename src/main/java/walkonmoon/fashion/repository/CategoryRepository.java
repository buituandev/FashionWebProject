package walkonmoon.fashion.repository;

import org.springframework.data.repository.CrudRepository;
import walkonmoon.fashion.model.Category;

public interface CategoryRepository extends CrudRepository<Category,Integer> {
}

