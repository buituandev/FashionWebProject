package walkonmoon.fashion.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItemDTO {
    private String id;
    private String title;
    private String image;
    private String price;
    private String stock;
    private int quantity;
}
