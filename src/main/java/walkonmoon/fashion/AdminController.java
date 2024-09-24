package walkonmoon.fashion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/admin/index.html")
    public String dashboard() {
        return "admin/index";
    }

    @GetMapping("/product-management")
    public String productManagement() {
        return "admin/eco-products";
    }

    @GetMapping("/category-management")
    public String categoryManagement() {
        return "admin/eco-products-detail";
    }

    @GetMapping("/order-management")
    public String orderManagement() {
        return "admin/eco-products-orders";
    }
}