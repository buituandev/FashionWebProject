package walkonmoon.fashion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import walkonmoon.fashion.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository <OrderDetail,Integer> {
}
