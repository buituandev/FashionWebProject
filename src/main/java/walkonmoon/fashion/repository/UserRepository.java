package walkonmoon.fashion.repository;

import org.springframework.data.repository.CrudRepository;
import walkonmoon.fashion.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
User findByEmail(String email);
User findByToken(String token);
}
