package walkonmoon.fashion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import walkonmoon.fashion.model.Category;
import walkonmoon.fashion.model.Image;
import walkonmoon.fashion.model.Product;
import walkonmoon.fashion.model.User;
import walkonmoon.fashion.service.CategoryService;
import walkonmoon.fashion.service.ImageService;
import walkonmoon.fashion.service.ProductService;
import walkonmoon.fashion.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ClientController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String index(Model model){
        List<Product> products = productService.getListProducts();
        model.addAttribute("products", products);
        List<Product> filteredProductByCategory1 = productService.findByCategoryId(1);
        model.addAttribute("filteredProductByCategory1", filteredProductByCategory1);
        List<Product> filteredProductByCategory2 = productService.findByCategoryId(2);
        model.addAttribute("filteredProductByCategory2", filteredProductByCategory2);
        List<Product> filteredProductByCategory3 = productService.findByCategoryId(3);
        model.addAttribute("filteredProductByCategory3", filteredProductByCategory3);
        List<Product> filteredProductByCategory4 = productService.findByCategoryId(4);
        model.addAttribute("filteredProductByCategory4", filteredProductByCategory4);
        return "index";

    }


    @GetMapping("/index.html")
    public String indexHtml(Model model) {
        List<Product> products = productService.getListProducts();
        model.addAttribute("products", products);
        List<Product> filteredProductByCategory1 = productService.findByCategoryId(1);
        model.addAttribute("filteredProductByCategory1", filteredProductByCategory1);
        List<Product> filteredProductByCategory2 = productService.findByCategoryId(2);
        model.addAttribute("filteredProductByCategory2", filteredProductByCategory2);
        List<Product> filteredProductByCategory3 = productService.findByCategoryId(3);
        model.addAttribute("filteredProductByCategory3", filteredProductByCategory3);
        List<Product> filteredProductByCategory4 = productService.findByCategoryId(4);
        model.addAttribute("filteredProductByCategory4", filteredProductByCategory4);
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        return "index";
    }

    @GetMapping("/category")
    public String category() {
        return "shop-grid";
    }

    @GetMapping("/shop-grid.html")
    public String shopGridHtml(Model model){
        List<Product> products = productService.getListProducts();
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "shop-grid";
    }
    @GetMapping("/shop-grid/{categoryId}")
    public String filterShopGrid(@PathVariable int categoryId, Model model){
        List<Product> filteredProduct = productService.findByCategoryId(categoryId);
        model.addAttribute("products", filteredProduct);
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);
        return "shop-grid";
    }

    @GetMapping("/product-detail")
    public String productDetail() {
        return "single-product";
    }
    
    @GetMapping("/single-product/{id}")
    public String singleProduct(Model model, @PathVariable String id){
        Product product = productService.getProductById(Integer.parseInt(id));
        model.addAttribute("product", product);
        List<Image> productImages = imageService.findByProductId(product.getId());
        model.addAttribute("productImages", productImages);
        List<Product> relatedProducts = productService.findByCategoryId(product.getCategoryId());
        relatedProducts = relatedProducts.stream()
                .filter(p -> p.getId() != product.getId())
                .collect(Collectors.toList());
        model.addAttribute("relatedProducts", relatedProducts);
        return "single-product";
    }

    @GetMapping("/categories")
    public String getCategories(Model model) {
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        return "layout"; // or the name of your template
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

    @GetMapping("/upload.html")
    public String uploadHtml(){
        return "upload";
    }

}