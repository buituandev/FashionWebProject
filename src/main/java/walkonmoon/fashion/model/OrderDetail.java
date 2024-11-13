package walkonmoon.fashion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orderdetail")
public class OrderDetail {
    @Id
    private int id;
    @Column(name = "order_id")
    private int orderID;
    @Column(name = "product_id")
    private int productID;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private int price;
}
