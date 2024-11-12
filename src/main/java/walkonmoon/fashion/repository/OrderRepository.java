package walkonmoon.fashion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import walkonmoon.fashion.model.Order;

public interface OrderRepository extends JpaRepository<Order,Integer> {
}
