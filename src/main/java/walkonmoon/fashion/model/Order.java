package walkonmoon.fashion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "customer_id")
    private int userID;
    @Column(name = "order_date")
    private Date order_date;
    @Column(name = "total_price")
    private int total_price;
    @Column(name = "note")
    private String note;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "address")
    private String address;
    @Convert(converter = walkonmoon.fashion.model.OrderStatusConverter.class)
    @Column(name = "status")
    private OrderStatus status;
}
