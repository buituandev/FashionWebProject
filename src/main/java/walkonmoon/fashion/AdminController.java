package walkonmoon.fashion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import walkonmoon.fashion.model.Category;
import walkonmoon.fashion.model.Product;
import walkonmoon.fashion.model.User;
import walkonmoon.fashion.service.CategoryService;
import walkonmoon.fashion.service.ProductService;
import walkonmoon.fashion.service.UserService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/index.html")
    public String dashboard() {
        return "admin/index";
    }

    @GetMapping("/eco-products.html")
    public String productManagement(Model model2) {
        List<Product> products = productService.getListProducts();
        model2.addAttribute("productList", products);
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
    public String formProduct(Model model) {
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());
        return "admin/eco-products-edit";
    }

    @GetMapping("/category-add.html")
    public String formCategory(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category-add";
    }

    @GetMapping("/category.html")
    public String categoryManagement(Model model) {
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        return "admin/category";
    }

    @GetMapping("/user-management.html")
    public String userManagement(Model model) {
        List<User> users = userService.getListUser(); // Fetch all users
        model.addAttribute("userList", users);
        return "admin/user-management";
    }

    @PostMapping("/eco-products/save")
    public String saveProduct(Product product, Model model) {
        Date updatedNow = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        product.setUpdate_date(updatedNow);
        product.setImage_collection_url("assets/images/products/new/product-10.jpg");
        productService.saveProduct(product);
        return "redirect:/admin/eco-products.html";
    }

    @PostMapping("/category/save")
    public String saveCategory(Category category, Model model) {
        Date updatedNow = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        category.setCreateDate(updatedNow);
        categoryService.saveCategory(category);
        return "redirect:/admin/category.html";
    }

    @GetMapping("/category/category-edit/{id}")
    public String editCategory(@PathVariable("id") Integer id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "admin/category-add";
    }

    @GetMapping("/category/category-delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id){
        categoryService.deleteCategoryById(id);
        return "redirect:/admin/category.html";
    }
}