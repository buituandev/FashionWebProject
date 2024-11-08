package walkonmoon.fashion.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import walkonmoon.fashion.dto.CartItemDTO;
import walkonmoon.fashion.model.Category;
import walkonmoon.fashion.model.User;
import walkonmoon.fashion.service.CartItemService;
import walkonmoon.fashion.service.CategoryService;
import walkonmoon.fashion.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CustomErrorController implements ErrorController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService;

    @RequestMapping("/error")
    public String handleError(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Integer userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if (!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        // chunk categories
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        // Existing logic for cart items
        List<CartItemDTO> cartItems = new ArrayList<>();
        String userId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userID".equals(cookie.getName())) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }

        if (userId != null) {
            cartItems = cartItemService.getCartItems(Integer.parseInt(userId));
        }

        int totalPrice = cartItemService.calculateTotalPrice(cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartItems", cartItems);
        return "404";
    }
}