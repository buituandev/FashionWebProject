package walkonmoon.fashion;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import walkonmoon.fashion.model.Category;
import walkonmoon.fashion.model.Image;
import walkonmoon.fashion.model.Product;
import walkonmoon.fashion.model.User;
import walkonmoon.fashion.service.CategoryService;
import walkonmoon.fashion.service.ImageService;
import walkonmoon.fashion.service.ProductService;
import walkonmoon.fashion.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;
    private final FirebaseConfig firebaseConfig;
    @Autowired
    private ImageService imageService;

    public AdminController(FirebaseConfig firebaseConfig) {
        this.firebaseConfig = firebaseConfig;
    }

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

    @GetMapping("/eco-products-edit.html")
    public String formProduct(Model model) {
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());

        return "admin/eco-products-edit";
    }

    @PostMapping("/eco-products/save")
    public String saveProduct(@ModelAttribute Product product, Model model,
                              @RequestParam("files") MultipartFile[] files,
                              RedirectAttributes redirectAttributes) {
        Date updatedNow = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        product.setUpdate_date(updatedNow);

        Product existPro = productService.getProductById(product.getId());
//        if(existPro == null) {
//            imageService.deleteByProductId(product.getId());
//        }else{
//            productService.saveProduct(product);
//        }
        if(existPro == null){
            productService.saveProduct(product);
        }

        if (files != null && files.length > 1) {
            imageService.deleteByProductId(product.getId());
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isEmpty()) {
                    try (InputStream inputStream = files[i].getInputStream()) {
                        String fileUrl = getFileName(files[i], inputStream);
                        Image image = new Image();
                        image.setImageurl(fileUrl);
                        image.setProductId(product.getId());
                        imageService.saveImage(image);
                        if(i == 0){
                            product.setImage_collection_url(fileUrl);
                        }
                    } catch (IOException e) {
                        redirectAttributes.addFlashAttribute("message", "Failed to upload file");
                        return "redirect:/admin/eco-products-edit.html";
                    }
                }else {

                }
            }
        }

        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("message", "Product saved successfully");
        return "redirect:/admin/eco-products.html";
    }

    @GetMapping("/category-add.html")
    public String formCategory(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category-add";
    }

    @PostMapping("/category/save")
    public String saveCategory(Category category, Model model) {
        Date updatedNow = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        category.setCreateDate(updatedNow);
        categoryService.saveCategory(category);
        return "redirect:/admin/category.html";
    }

    @GetMapping("/category-edit/{id}")
    public String editCategory(@PathVariable("id") Integer id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "admin/category-add";
    }

    @GetMapping("/category-delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id){
        categoryService.deleteCategoryById(id);
        return "redirect:/admin/category.html";
    }

    @GetMapping("/eco-products-edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model) {
        Product product = productService.getProductById(id);
        List<Category> categories = categoryService.getListCategories();
        List<Image> images = imageService.findImageByProductId(id);
        model.addAttribute("categories", categories);
        model.addAttribute("images", images);
        model.addAttribute("product", product);
        return "admin/eco-products-edit";
    }

    @GetMapping("/product-delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, Model model) {
        imageService.deleteByProductId(id);
        productService.deleteProductById(id);
        return "redirect:/admin/eco-products.html";
    }

    private String getFileName(MultipartFile file, InputStream inputStream) {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Blob blob = StorageClient.getInstance().bucket().create(fileName, inputStream, file.getContentType());
        blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", firebaseConfig.getBucketName(), fileName);
    }
}