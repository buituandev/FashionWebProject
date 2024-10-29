package walkonmoon.fashion.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import walkonmoon.fashion.model.CartItemDTO;
import walkonmoon.fashion.service.CartItemService;

import java.util.List;

@RestController
public class CartController {
    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/merge")
    public void mergeLocalStorageCart(@RequestBody MergeRequest mergeRequest) {
        cartItemService.mergeLocalStorageCart(mergeRequest.getUserId(), mergeRequest.getLocalCartItems());
    }

    @GetMapping("/cart/{userId}")
    public List<CartItemDTO> getCart(@PathVariable int userId) {
        return cartItemService.getCartItemsDTOByUserId(userId);
    }

    @Setter
    @Getter
    public static class MergeRequest {
        private Integer userId;
        private List<CartItemDTO> localCartItems;

    }
}