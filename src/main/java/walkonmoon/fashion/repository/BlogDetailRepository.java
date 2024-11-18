package walkonmoon.fashion.repository;

import org.springframework.data.repository.CrudRepository;
import walkonmoon.fashion.model.BlogDetail;

import java.util.Optional;

public interface BlogDetailRepository extends CrudRepository<BlogDetail,Integer> {
    Optional<BlogDetail> findByBlogID(int id);
}
