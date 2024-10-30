package walkonmoon.fashion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItemDTO {
    private int id;
    private String title;
    private String image;
    private int price;
    private int stock;
    private int quantity;
    private int userId;
}
