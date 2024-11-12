package walkonmoon.fashion.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.OrderDetail;
import walkonmoon.fashion.repository.OrderDetailRepository;

import java.util.List;

@Service
@Transactional
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public void saveOrderDetail(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }

    public List<OrderDetail> getOrderDetailByOrderID(int id) {
        return orderDetailRepository.findByOrderID(id);
    }
}
