package walkonmoon.fashion.repository;

import org.springframework.data.repository.CrudRepository;
import walkonmoon.fashion.model.Blog;

public interface BlogRepository extends CrudRepository<Blog,Integer> {
}
