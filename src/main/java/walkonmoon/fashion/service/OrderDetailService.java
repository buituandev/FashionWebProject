package walkonmoon.fashion.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.OrderDetail;
import walkonmoon.fashion.repository.OrderDetailRepository;

@Service
@Transactional
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public void saveOrderDetail(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }
}
