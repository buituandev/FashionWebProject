package walkonmoon.fashion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import walkonmoon.fashion.model.OrderDetail;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository <OrderDetail,Integer> {
    List<OrderDetail> findByOrderID(int id);
}
