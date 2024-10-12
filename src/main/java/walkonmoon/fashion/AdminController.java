package walkonmoon.fashion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import walkonmoon.fashion.model.User;
import walkonmoon.fashion.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;


    @GetMapping("/index.html")
    public String dashboard() {
        return "admin/index";
    }

    @GetMapping("/eco-products.html")
    public String productManagement() {
        return "admin/eco-products";
    }


    @GetMapping("/edo-products-detail.html")
    public String productDetail() {
        return "admin/eco-products-detail";
    }


    @GetMapping("/eco-products-orders.html")
    public String orderManagement() {
        return "admin/eco-products-orders";
    }

    @GetMapping("/eco-products-edit.html")
    public String editProduct() {
        return "admin/eco-products-edit";
    }

    @GetMapping("/category.html")
    public String categoryManagement() {
        return "admin/category";
    }

    @GetMapping("/user-management.html")
    public String userManagement(Model model) {
        List<User> users = userService.getAll(); // Fetch all users
        model.addAttribute("users", users);
        return "admin/user-management";
    }
}