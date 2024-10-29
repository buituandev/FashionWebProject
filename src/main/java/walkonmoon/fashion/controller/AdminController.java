package walkonmoon.fashion.controller;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import walkonmoon.fashion.config.FirebaseConfig;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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
        Map<Integer, String> categoryMap = new HashMap<>();
         for (Product product : products) {
              Category category = categoryService.getCategoryById(product.getCategoryId());
              if(category != null){
                   categoryMap.put(product.getCategoryId(), category.getName());
              }
         }
        model2.addAttribute("productList", products);
         model2.addAttribute("categoryMap", categoryMap);

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
        model.addAttribute("mode", "create");
        return "admin/eco-products-edit";
    }

    @PostMapping("/eco-products/save")
    public String saveProduct(@ModelAttribute Product product, Model model,
                              @RequestParam("files") MultipartFile[] files, @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        Date updatedNow = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        product.setUpdate_date(updatedNow);

        Product existPro = productService.getProductById(product.getId());
        if(existPro == null){
            productService.saveProduct(product);
        }else {
            product.setImage_collection_url(existPro.getImage_collection_url());  // Keep existing URL if no new file
        }
        if (!file.isEmpty()) {
            try (InputStream inputStream = file.getInputStream()) {
                String fileUrl = getFileName(file, inputStream);
                product.setImage_collection_url(fileUrl);
            } catch (IOException e) {
                return null;
            }
        }

        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isEmpty()) {
                    try (InputStream inputStream = files[i].getInputStream()) {
                        String fileUrl = getFileName(files[i], inputStream);
                        Image image = new Image();
                        image.setImageurl(fileUrl);
                        image.setProductId(product.getId());
                        imageService.saveImage(image);
                    } catch (IOException e) {
                        redirectAttributes.addFlashAttribute("message", "Failed to upload file");
                        return "redirect:/admin/eco-products-edit.html";
                    }
                }else {

                }
            }
        }

        productService.saveProduct(product);
        Category category = categoryService.getCategoryById(product.getCategoryId());
        if (category != null) {
            category.setQuantity(category.getQuantity() + 1); // Increase quantity by 1
            categoryService.saveCategory(category); // Save updated category
        }
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
    public String deleteCategory(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        Category category = categoryService.getCategoryById(id);

        if (category.getQuantity() > 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete category with products in it.");
            return "redirect:/admin/category.html"; // Redirect back to the category page
        }

        categoryService.deleteCategoryById(id); // Proceed with deletion
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
        model.addAttribute("mode", "edit");
        Category category = categoryService.getCategoryById(product.getCategoryId());
        if (category != null && category.getQuantity() > 0) {
            category.setQuantity(category.getQuantity() - 1); // Decrease quantity by 1
            categoryService.saveCategory(category); // Save updated category
        }

        return "admin/eco-products-edit";
    }

    @GetMapping("/product-delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, Model model) {
        String fileUrl =  productService.getProductById(id).getImage_collection_url();
        ResponseEntity<String> deleteImg = deleteFile(fileUrl);
        for(Image img : imageService.findByProductId(id)){
            String url = img.getImageurl();
            ResponseEntity<String> delete = deleteFile(url);
        }

        if (deleteImg.getStatusCode() != HttpStatus.OK) {
            System.out.println("Failed to delete file from Firebase: " + deleteImg.getBody());
        }
        imageService.deleteByProductId(id);
        productService.deleteProductById(id);
        Category category = categoryService.getCategoryById(productService.getProductById(id).getCategoryId());
        if (category != null && category.getQuantity() > 0) {
            category.setQuantity(category.getQuantity() - 1); // Decrease quantity by 1
            categoryService.saveCategory(category); // Save updated category
        }

        return "redirect:/admin/eco-products.html";
    }

    @GetMapping("/pages-login.html")
    public  String loginPages(Model model){
        model.addAttribute( "user", new User());
        return "/admin/pages-login";
    }

    @PostMapping("/login")
    public  String login(@RequestParam("email") String email, @RequestParam("password") String password, RedirectAttributes redirectAttributes){
         User user = userService.getUserByEmail(email);
        if(user == null){
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid email or password.");
            return "redirect:/admin/pages-login.html";
        }
         else if(user.getPassword().equals(password)){
             return "redirect:/admin/index.html";
         }
        return "redirect:/admin/pages-login.html";
    }

    private String getFileName(MultipartFile file, InputStream inputStream) {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Blob blob = StorageClient.getInstance().bucket().create(fileName, inputStream, file.getContentType());
        blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", firebaseConfig.getBucketName(), fileName);
    }

    private ResponseEntity<String> deleteFile(String fileUrl) {
        try {
            String fileName = extractFileName(fileUrl);
            BlobId blobId = BlobId.of(firebaseConfig.getBucketName(), fileName);
            boolean deleted = StorageClient.getInstance().bucket().getStorage().delete(blobId);
            if (deleted) {
                return new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String extractFileName(String fileUrl) {
        String[] parts = fileUrl.split("/");
        String fileName = parts[parts.length - 1].split("\\?")[0];
        return fileName;
    }

}