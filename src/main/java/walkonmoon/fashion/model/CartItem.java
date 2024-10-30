package walkonmoon.fashion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@IdClass(CartItemId.class)
@Entity
@Table(name = "cartitem")
public class CartItem {

    @Id
    @Column(name = "product_id")
    private int productId;

    @Column(name = "quantity")
    private int quantity;

    @Id
    @Column(name = "user_id")
    private int userId;

}
