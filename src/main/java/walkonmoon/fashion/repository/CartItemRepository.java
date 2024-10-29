package walkonmoon.fashion.repository;

import org.springframework.data.repository.CrudRepository;
import walkonmoon.fashion.model.CartItem;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem,Integer> {
    CartItem findByUserIdAndProductId(int userId, int productId);
    List<CartItem> findByUserId(int userId);
    void deleteByUserIdAndProductId(int userId, int productId);
}
