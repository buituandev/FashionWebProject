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

    @GetMapping("/category")
    public String category() {
        return "shop-grid";
    }

    @GetMapping("/shop-grid.html")
    public String shopGridHtml(){
        return "shop-grid";
    }

    @GetMapping("/product-detail")
    public String productDetail() {
        return "single-product";
    }

    @GetMapping("/single-product.html")
    public String singleProductHtml(){
        return "single-product";
    }

    @GetMapping("/shopping-cart")
    public String shoppingCart() {
        return "cart";
    }

    @GetMapping("/cart.html")
    public String cartHtml(){
        return "cart";
    }

    @GetMapping("/shipping-page")
    public String shippingPage() {
        return "checkout";
    }

    @GetMapping("/checkout.html")
    public String checkoutHtml(){
        return "checkout";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login.html")
    public String loginHtml(){
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
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

    @GetMapping("/blog")
    public String blog() {
        return "blog-right-sidebar";
    }

    @GetMapping("/blog-right-sidebar.html")
    public String blogLeftSidebarHtml(){
        return "blog-right-sidebar";
    }

    @GetMapping("/blog-detail")
    public String blogDetail() {
        return "blog-details";
    }

    @GetMapping("/blog-details.html")
    public String blogDetailsHtml(){
        return "blog-details";
    }

//    @GetMapping("/logout")
//    public String logout() {
//        return "logout";
//    }

//    @GetMapping("/forgot-password")
//    public String forgotPassword() {
//        return "forgot-password";
//    }

    @GetMapping("/profile")
    public String profile() {
        return "my-account";
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