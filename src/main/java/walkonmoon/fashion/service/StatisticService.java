package walkonmoon.fashion.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.Order;
import walkonmoon.fashion.model.OrderDetail;
import walkonmoon.fashion.model.OrderStatus;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class StatisticService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderDetailService orderDetailService;

    public int getTotalOrderPerMonth() {
        LocalDate now = LocalDate.now();
        List<Order> orders = orderService.getOrderList();
        return (int) orders.stream()
                .filter(order -> order.getOrder_date() != null)
                .filter(order -> order.getOrder_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth() == now.getMonth())
                .count();
    }

    public int getTotalPurchasedProductPerMonth() {
        LocalDate now = LocalDate.now();
        List<Order> orders = orderService.getOrderList();
        return orders.stream()
                .filter(order -> order.getOrder_date() != null)
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .filter(order -> order.getOrder_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth() == now.getMonth())
                .map(order -> orderDetailService.getOrderDetailByOrderID(order.getId()))
                .flatMap(Collection::stream)
                .mapToInt(OrderDetail::getQuantity)
                .sum();
    }
}
