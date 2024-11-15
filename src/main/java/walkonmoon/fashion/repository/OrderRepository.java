package walkonmoon.fashion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import walkonmoon.fashion.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByUserID(int id);
}