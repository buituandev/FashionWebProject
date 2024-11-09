package walkonmoon.fashion.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import walkonmoon.fashion.dto.CartItemDTO;
import walkonmoon.fashion.model.User;
import walkonmoon.fashion.service.CartItemService;

import java.io.IOException;
import java.util.*;

@Controller
public class CartController {
    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/cart")
    @ResponseBody
    public void addCartItem(@RequestParam("productId") int productId, @RequestParam("quantity") int quantity, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        String userId = user.getId().toString();

        if (userId.equals("null")) {
            response.sendError(401);
        } else {
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setUserId(Integer.parseInt(userId));
            cartItemDTO.setId(productId);
            cartItemDTO.setQuantity(quantity);
            cartItemService.addCartItem(cartItemDTO,response);
        }
    }

    @GetMapping("/getCart")
    public String getCartItems(Model model, HttpServletRequest request, HttpSession session) {
        List<CartItemDTO> cartItems = new ArrayList<>();
        User user = (User) session.getAttribute("user");
        String userId = user.getId().toString();

        if (!(userId.equals("null"))) {
            cartItems = cartItemService.getCartItems(Integer.parseInt(userId));
        }
        
        int totalPrice = cartItemService.calculateTotalPrice(cartItems);
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("cartItems", cartItems);
        return "layout";
    }
    
    @GetMapping("/deleteCartItem")
    public void deleteCartItem(@RequestParam("productId") int productId, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        String userId = user.getId().toString();
        if (userId.equals("null")) {
            response.sendRedirect("/login.html");
        } else {
            cartItemService.deleteCartItem(Integer.parseInt(userId), productId);
        }
    }
    
    @GetMapping("/updateCartItem")
    public void updateCartItem(@RequestParam("productId") int productId, @RequestParam("quantity") int quantity, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        String userId = user.getId().toString();
        if (userId.equals("null")) {
            response.sendRedirect("/login.html");
        } else {
            cartItemService.updateCartItem(Integer.parseInt(userId), productId, quantity, response);
        }
    }
}