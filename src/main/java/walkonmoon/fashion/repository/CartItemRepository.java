package walkonmoon.fashion.repository;

import org.springframework.data.repository.CrudRepository;
import walkonmoon.fashion.model.CartItem;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem,Integer> {
    List<CartItem> findByUserId(int userId);
}
