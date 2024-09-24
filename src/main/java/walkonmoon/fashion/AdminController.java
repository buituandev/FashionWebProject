package walkonmoon.fashion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/index.html")
    public String dashboard() {
        return "admin/index";
    }

    @GetMapping("/eco-products.html")
    public String productManagement() {
        return "admin/eco-products";
    }

    @GetMapping("/edo-products-detail.html")
    public String categoryManagement() {
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
}