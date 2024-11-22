package walkonmoon.fashion.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.Order;
import walkonmoon.fashion.repository.OrderRepository;
import walkonmoon.fashion.repository.ProductRepository;
import walkonmoon.fashion.model.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StatisticService {
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderDetailService orderDetailService;

    public StatisticService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        }
    public int getTotalOrderPerMonth() {
        LocalDate now = LocalDate.now();
        List<Order> orders = orderService.getOrderList();
        return (int) orders.stream()
                .filter(order -> order.getOrder_date() != null)
                .filter(order -> order.getOrder_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth() == now.getMonth())
                .count();
    }

    public int calculateOrderAverage() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        List<Order> ordersOfMonth = orderRepository.findAll().stream()
                .filter(order -> order.getOrder_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue()== currentMonth
                        && order.getOrder_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear() == currentYear)
                .toList();

        int daysUntilNow = currentDate.getDayOfMonth();

        double averageOrdersPerDay = ordersOfMonth.size() / (double) daysUntilNow;

        return (int)averageOrdersPerDay;
    }

    public int calculateTotalRevenue() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        List<Order> ordersOfMonth = orderRepository.findAll().stream()
                .filter(order -> order.getOrder_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue() == currentMonth
                        && order.getOrder_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear() == currentYear)
                .toList();
        int result=0;
        for(Order order : ordersOfMonth){
            result+= order.getTotal_price();
        }

        return result;
    public int getTotalPurchasedProductPerMonth(){
        LocalDate now = LocalDate.now();
        List<Order> orders = orderService.getOrderList();
        return (int) orders.stream()
                .filter(order -> order.getOrder_date() != null)
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .filter(order -> order.getOrder_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth() == now.getMonth())
                .map(order -> orderDetailService.getOrderDetailByOrderID(order.getId()))
                .mapToLong(Collection::size)
                .sum();
    }
}
