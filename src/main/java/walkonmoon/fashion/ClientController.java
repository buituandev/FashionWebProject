package walkonmoon.fashion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/index.html")
    public String indexHtml() {
        return "index";
    }

    @GetMapping("/shop-grid.html")
    public String shopGridHtml(){
        return "shop-grid";
    }

    @GetMapping("/single-product.html")
    public String singleProductHtml(){
        return "single-product";
    }

    @GetMapping("/cart.html")
    public String cartHtml(){
        return "cart";
    }

    @GetMapping("/checkout.html")
    public String checkoutHtml(){
        return "checkout";
    }

    @GetMapping("/login.html")
    public String loginHtml(){
        return "login";
    }

    @GetMapping("/register.html")
    public String registerHtml(){
        return "register";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/contact.html")
    public String contactHtml(){
        return "contact";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/about.html")
    public String aboutHtml(){
        return "about";
    }

    @GetMapping("/blog-left-sidebar.html")
    public String blogLeftSidebarHtml(){
        return "blog-left-sidebar";
    }

    @GetMapping("/blog-detail")
    public String blogDetail() {
        return "blog-details";
    }

    @GetMapping("/blog-details.html")
    public String blogDetailsHtml(){
        return "blog-details";
    }

    @GetMapping("/my-account.html")
    public String myAccountHtml(){
        return "my-account";
    }

    @GetMapping("wishlist.html")
    public String wishlistHtml(){return "wishlist";}

    @GetMapping("compare.html")
    public String compareHtml(){return "compare";}

}