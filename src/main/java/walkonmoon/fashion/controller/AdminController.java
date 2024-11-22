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
import walkonmoon.fashion.model.*;
import walkonmoon.fashion.service.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogDetailService blogDetailService;
    @Autowired
    private StatisticService statisticService;

    public AdminController(FirebaseConfig firebaseConfig) {
        this.firebaseConfig = firebaseConfig;
    }

    @GetMapping("/index.html")
    public String dashboard(Model model) {
        int averageOrderPerDayOfMonth = statisticService.calculateOrderAverage();
        model.addAttribute("averageOrderPerDayOfMonth", averageOrderPerDayOfMonth);
        int totalRevenue = statisticService.calculateTotalRevenue();
        model.addAttribute("totalRevenue", totalRevenue);
        System.out.println("tRevenue "+totalRevenue);
        return "admin/index";
    }

    @GetMapping("/eco-products.html")
    public String productManagement(Model model2) {
        List<Product> products = productService.getListProducts();
        Map<Integer, String> categoryMap = new HashMap<>();
        for (Product product : products) {
            Category category = categoryService.getCategoryById(product.getCategoryId());
            if (category != null) {
                categoryMap.put(product.getCategoryId(), category.getName());
            }
        }
        model2.addAttribute("productList", products);
        model2.addAttribute("categoryMap", categoryMap);

        return "admin/eco-products";
    }


    @GetMapping("/eco-products-detail.html")
    public String productDetail() {
        return "admin/eco-products-detail";
    }


    @GetMapping("/eco-products-orders.html")
    public String orderManagement(Model model) {
        List<Order> orders = orderService.getOrderList();
        Map<Integer, String> userName = new HashMap<>();
        for (User user : userService.getListUser()) {
            userName.put(user.getId(), user.getFull_name());
        }
        model.addAttribute("userName", userName);
        model.addAttribute("orderList", orders);
        return "admin/eco-products-orders";
    }

    @GetMapping("/eco-order-detail/{id}")
    public String orderDetail(@PathVariable("id") Integer id, Model model) {
        List<OrderDetail> orderdetail = orderDetailService.getOrderDetailByOrderID(id);
        List<Product> products = new ArrayList<>();
        for (OrderDetail orderDetail : orderdetail) {
            Product product = productService.getProductById(orderDetail.getProductID());
            products.add(product);
        }
        model.addAttribute("productList", products);
        model.addAttribute("orderItems", orderdetail);
        return "admin/eco-order-detail";
    }

    @PostMapping("/update-order-status/{id}")
    @ResponseBody
    public ResponseEntity<?> updateOrderStatus(@PathVariable("id") Integer id, @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");

        Order order = orderService.getOrderbyOrderID(id);
        order.setStatus(OrderStatus.valueOf(newStatus));
        orderService.saveOrder(order);

        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    @GetMapping("/eco-products-edit.html")
    public String formProduct(Model model) {
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());
        model.addAttribute("mode", "create");
        return "admin/eco-products-edit";
    }

    //save images local

    //    @PostMapping("/eco-products/save")
//    public String saveProduct(@ModelAttribute Product product, Model model,
//                              @RequestParam("files") MultipartFile[] files,
//                              @RequestParam("file") MultipartFile file,
//                              RedirectAttributes redirectAttributes) {
//        Date updatedNow = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
//        product.setUpdate_date(updatedNow);
//
//        // Save or update product information
//        Product existPro = productService.getProductById(product.getId());
//        if (existPro == null) {
//            productService.saveProduct(product);
//            Category newCategory = categoryService.getCategoryById(product.getCategoryId());
//            if (newCategory != null) {
//                newCategory.setQuantity(newCategory.getQuantity() + 1);
//                categoryService.saveCategory(newCategory);
//            }
//        } else {
//            product.setImage_collection_url(existPro.getImage_collection_url());
//        }
//
//        // Define upload folder path
//        String folder = "src/main/resources/static/assets/upload";
//        File uploadDir = new File(folder);
//        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
//            redirectAttributes.addFlashAttribute("message", "Failed to create upload directory");
//            return "redirect:/admin/eco-products-edit.html";
//        }
//
//        try {
//            // Save the main image with Imgscalr
//            if (!file.isEmpty()) {
//                String fileUrl = saveImageWithImgScalr(file, folder);
//                product.setImage_collection_url(fileUrl);
//            }
//
//            // Save additional images
//            if (files != null && files.length > 0) {
//                for (MultipartFile multipartFile : files) {
//                    if (!multipartFile.isEmpty()) {
//                        String fileUrl = saveImageWithImgScalr(multipartFile, folder);
//                        Image image = new Image();
//                        image.setImageurl(fileUrl);
//                        image.setProductId(product.getId());
//                        imageService.saveImage(image);
//                    }
//                }
//            }
//            productService.saveProduct(product);
//            Integer originalCategoryId = existPro != null ? existPro.getCategoryId() : null;
//            if (originalCategoryId != null && !originalCategoryId.equals(product.getCategoryId())) {
//                Category originalCategory = categoryService.getCategoryById(originalCategoryId);
//                if (originalCategory != null) {
//                    originalCategory.setQuantity(originalCategory.getQuantity() - 1);
//                    categoryService.saveCategory(originalCategory);
//                }
//
//                Category newCategory = categoryService.getCategoryById(product.getCategoryId());
//                if (newCategory != null) {
//                    newCategory.setQuantity(newCategory.getQuantity() + 1);
//                    categoryService.saveCategory(newCategory);
//                }
//            }
//            redirectAttributes.addFlashAttribute("product", product);
//            redirectAttributes.addFlashAttribute("confirmMessage", "Product saved successfully");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            redirectAttributes.addFlashAttribute("message", "Failed to upload images: " + e.getMessage());
//            return "redirect:/admin/eco-products-edit.html";
//        }
//
//        return "redirect:/admin/eco-products.html";
//    }
//
//    private String saveImageWithImgScalr(MultipartFile file, String folderPath) throws IOException {
//        String originalFileName = file.getOriginalFilename();
//        String fileExtension = "";
//
//        // Extract file extension
//        if (originalFileName != null && originalFileName.lastIndexOf(".") != -1) {
//            fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
//        }
//
//        // Set output format and file name
//        String outputFormat = fileExtension.equals("webp") ? "jpg" : fileExtension;
//        String fileName = (fileExtension.equals("webp") ? originalFileName.replace(".webp", ".jpg") : originalFileName);
//        String filePath = folderPath + File.separator + fileName;
//
//        // Read image, handle WebP conversion if necessary
//        BufferedImage originalImage = ImageIO.read(file.getInputStream());
//
//        if (originalImage == null) {
//            throw new IOException("Failed to read image file: " + originalFileName);
//        }
//
//        // Resize image using Imgscalr
//        BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, 300, 300);
//
//        // Save image with chosen format
//        File outputFile = new File(filePath);
//        if (!ImageIO.write(resizedImage, outputFormat, outputFile)) {
//            throw new IOException("Failed to save image as " + outputFormat + ": " + filePath);
//        }
//
//        return "/assets/upload/" + fileName;
//    }
    @GetMapping("/eco-products-edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model) {
        Product product = productService.getProductById(id);
        List<Category> categories = categoryService.getListCategories();
        List<Image> images = imageService.findImageByProductId(id);
        model.addAttribute("categories", categories);
        model.addAttribute("images", images);
        model.addAttribute("product", product);
        model.addAttribute("mode", "edit");
        Category currentCategory = categoryService.getCategoryById(product.getCategoryId());
        return "admin/eco-products-edit";
    }

    @GetMapping("/product-delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        String fileUrl = productService.getProductById(id).getImage_collection_url();
        Product product = productService.getProductById(id);
        List<CartItem> cartitem = cartItemService.getAllCartItems();
//        for(CartItem cart : cartitem){
//            if(cart.getProductId() == id){
//                product.setStatus(ProductStatus.DISABLE);
//                productService.saveProduct(product);
//                redirectAttributes.addFlashAttribute("errorMessage",  "The product already in customer cart");
//                return "redirect:/admin/eco-products.html";
//            }
//            }
//        }
//        Path path = Paths.get("/ProjectB/FashionWebProject/src/main/resources/static" + fileUrl);;

//        ResponseEntity<String> deleteImg = deleteFile(fileUrl);
//        for (Image img : imageService.findByProductId(id)) {
//            String url = img.getImageurl();
//            ResponseEntity<String> delete = deleteFile(url);
//        }
//
//        if (deleteImg.getStatusCode() != HttpStatus.OK) {
//            System.out.println("Failed to delete file from Firebase: " + deleteImg.getBody());
//        }
//        imageService.deleteByProductId(id);

        Category category = categoryService.getCategoryById(productService.getProductById(id).getCategoryId());
        if (category.getQuantity() > 0) {
            category.setQuantity(category.getQuantity() - 1);
            categoryService.saveCategory(category);
        }
        product.setStatus(ProductStatus.DISABLE);
        productService.saveProduct(product);
//        productService.deleteProductById(id);


//        if (Files.exists(path)) {
//            try {
//                Files.delete(path);
//                for (Image img : imageService.findByProductId(id)) {
//                    String url = img.getImageurl();
//                    Path path2 = Paths.get("/ProjectB/FashionWebProject/src/main/resources/static" + url);
//                    if (Files.exists(path2)) {
//                        Files.delete(path2);
//                    }
//                }
//
//                imageService.deleteByProductId(id);
//                Category category = categoryService.getCategoryById(productService.getProductById(id).getCategoryId());
//                if (category.getQuantity() > 0) {
//                    category.setQuantity(category.getQuantity() - 1);
//                    categoryService.saveCategory(category);
//                }
//                productService.deleteProductById(id);
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }

        redirectAttributes.addFlashAttribute("successMessage", "Product disable successfully");
        return "redirect:/admin/eco-products.html";
    }

    @PostMapping("/delete-more-products")
    public ResponseEntity<?> deleteMultipleProducts(@RequestBody Map<String, List<Integer>> payload) {
        List<Integer> productIds = payload.get("productIds");
        if (productIds.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "No products selected for deletion"));
        }

        try {
            for (Integer id : productIds) {
                List<CartItem> cartItem = cartItemService.getAllCartItems();
                Product product = productService.getProductById(id);
//                for(CartItem cart : cartItem){
//                    if(cart.getProductId() == id){
//                        product.setStatus(ProductStatus.DISABLE);
//                        productService.saveProduct(product);
//                        return ResponseEntity.badRequest().body(Collections.singletonMap("success", "Product disable successfully"));
//                    }
//                }
//                imageService.deleteByProductId(id);
                Category category = categoryService.getCategoryById(productService.getProductById(id).getCategoryId());
                if (category.getQuantity() > 0) {
                    category.setQuantity(category.getQuantity() - 1);
                    categoryService.saveCategory(category);
                }
                product.setStatus(ProductStatus.DISABLE);
                productService.saveProduct(product);
            }

//            productService.deleteProducts(productIds);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Products disable successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Failed to delete products"));
        }
    }


    @PostMapping("/eco-products/save")
    public String saveProduct(@ModelAttribute Product product, Model model,
                              @RequestParam("files") MultipartFile[] files, @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        Date updatedNow = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        product.setUpdate_date(updatedNow);

        Product existPro = productService.getProductById(product.getId());
        if (existPro == null) {
            productService.saveProduct(product);
            Category newCategory = categoryService.getCategoryById(product.getCategoryId());
            if (newCategory != null) {
                newCategory.setQuantity(newCategory.getQuantity() + 1);
                categoryService.saveCategory(newCategory);
            }
        } else {
            product.setImage_collection_url(existPro.getImage_collection_url());
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
                }
            }
        }


        productService.saveProduct(product);
        Integer originalCategoryId;
        if (existPro != null) {
            originalCategoryId = existPro.getCategoryId();
        } else {
            originalCategoryId = null;
        }
        if (originalCategoryId != null && !originalCategoryId.equals(product.getCategoryId())) {
            if (product.getStatus().equals(existPro.getStatus()) && existPro.getStatus().equals(ProductStatus.ENABLE)) {
                Category category = categoryService.getCategoryById(originalCategoryId);
                if (category != null) {
                    category.setQuantity(category.getQuantity() - 1);
                    categoryService.saveCategory(category);
                }

                Category newCategory = categoryService.getCategoryById(product.getCategoryId());
                if (newCategory != null) {
                    newCategory.setQuantity(newCategory.getQuantity() + 1);
                    categoryService.saveCategory(newCategory);
                }
            } else if (existPro.getStatus().equals(ProductStatus.DISABLE) && product.getStatus().equals(ProductStatus.ENABLE)) {
                Category newCategory = categoryService.getCategoryById(product.getCategoryId());
                if (newCategory != null) {
                    newCategory.setQuantity(newCategory.getQuantity() + 1);
                    categoryService.saveCategory(newCategory);
                }
            } else if (existPro.getStatus().equals(ProductStatus.ENABLE) && product.getStatus().equals(ProductStatus.DISABLE)) {
                Category category = categoryService.getCategoryById(existPro.getCategoryId());
                if (category != null) {
                    category.setQuantity(category.getQuantity() - 1);
                    categoryService.saveCategory(category);
                }
            }
        } else if (originalCategoryId != null) {
            if (product.getStatus().equals(ProductStatus.ENABLE) && product.getStatus() != existPro.getStatus()) {
                Category category = categoryService.getCategoryById(originalCategoryId);
                if (category != null) {
                    category.setQuantity(category.getQuantity() + 1);
                    categoryService.saveCategory(category);
                }

            } else {
                Category category = categoryService.getCategoryById(originalCategoryId);
                if (category != null) {
                    category.setQuantity(category.getQuantity() - 1);
                    categoryService.saveCategory(category);
                }
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "Product saved successfully");
        return "redirect:/admin/eco-products.html";
    }

    @GetMapping("/category.html")
    public String categoryManagement(Model model) {
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        return "admin/category";
    }

    @GetMapping("/category-add.html")
    public String formCategory(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("mode", "create");
        return "admin/category-add";
    }

    @PostMapping("/category/save")
    public String saveCategory(Category category, Model model, RedirectAttributes redirectAttributes) {
        Date updatedNow = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        category.setCreateDate(updatedNow);
        Category existCate = categoryService.getCategoryById(category.getId());
        if (existCate != null) {
            category.setQuantity(existCate.getQuantity());
        }
        redirectAttributes.addFlashAttribute("confirmMessage", "Category saved successfully");
        categoryService.saveCategory(category);
        return "redirect:/admin/category.html";
    }

    @GetMapping("/category-edit/{id}")
    public String editCategory(@PathVariable("id") Integer id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        model.addAttribute("mode", "edit");
        return "admin/category-add";
    }

    @GetMapping("/category-delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Category category = categoryService.getCategoryById(id);

        if (category.getQuantity() > 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete category with products in it.");
            return "redirect:/admin/category.html";
        }
        redirectAttributes.addFlashAttribute("confirmMessage", "Category deleted successfully");
        categoryService.deleteCategoryById(id);
        return "redirect:/admin/category.html";
    }

    @GetMapping("/user-management.html")
    public String userManagement(Model model) {
        List<User> users = userService.getListUser(); // Fetch all users
        model.addAttribute("userList", users);
        return "admin/user-management";
    }

    @PostMapping("/user-form/edit")
    public String userSave(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        User user1 = userService.findUserById(user.getId());
        user.setPassword(user1.getPassword());
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("successMessages", "User Information saved successfully");
        return "redirect:/admin/user-management.html";
    }

    @GetMapping("/pages-login.html")
    public String loginPages(Model model) {
        model.addAttribute("user", new User());
        return "/admin/pages-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByEmail(email);
        String passSHA1 = UserService.toSHA1(password);
        if (user == null || !user.getPassword().equals(passSHA1) || user.getType() != 1) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid email or password.");
            return "redirect:/admin/pages-login.html";
        }

        return "redirect:/admin/index.html";
    }

    @GetMapping("/blog-admin.html")
    public String blog(Model model) {
        List<Blog> blogList = blogService.getListBlogs();
        model.addAttribute("blogs", blogList);
        return "admin/blog-admin";
    }


    @GetMapping("/blog-form.html")
    public String formBlog(Model model) {
        model.addAttribute("blog", new Blog());
        model.addAttribute("mode", "create");
        return "admin/blog-form";
    }

    @PostMapping("/blog/save")
    private String saveBlog(@ModelAttribute  Blog blog, @RequestParam("content") String content, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        Date updatedNow = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Blog existBlog = blogService.getBlogById(blog.getId());
        blog.setDate(updatedNow);
        if(existBlog == null){
            blogService.saveBlog(blog);
        }else{
            blog.setThumbnail(existBlog.getThumbnail());
        }

        if (!file.isEmpty()) {
            try (InputStream inputStream = file.getInputStream()) {
                String fileUrl = getFileName(file, inputStream);
                blog.setThumbnail(fileUrl);
            } catch (IOException e) {
                return null;
            }
        }
        blogService.saveBlog(blog);
        int id = 0;
        BlogDetail blogDetail;
        if(existBlog != null){
            id = existBlog.getId();
            blogDetail = blogDetailService.getBlogDetailByBlogID(id);
        }else{
            id = blog.getId();
            blogDetail = new BlogDetail();
            blogDetail.setBlogID(id);
        }

        blogDetail.setContent(content);
        blogDetailService.saveBlogDetail(blogDetail);
        return "redirect:/admin/blog-admin.html";
    }

    @GetMapping("/blog-form/{id}")
    public String editBlog(@PathVariable("id") Integer id, Model model){
         Blog blog = blogService.getBlogById(id);
         BlogDetail blogDetail = blogDetailService.getBlogDetailByBlogID(blog.getId());
         String content = blogDetail.getContent();
         model.addAttribute("blog",  blog);
         model.addAttribute("content", content);
         model.addAttribute("mode", "edit");
         return  "admin/blog-form";
    }

    @GetMapping("/blog-delete/{id}")
    public String deleteBlog(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        int bdId = blogDetailService.getBlogDetailByBlogID(id).getId();
        blogDetailService.deleteBlogDetailById(bdId);

        Blog blog = blogService.getBlogById(id);
        ResponseEntity<String> response =  deleteFile(blog.getThumbnail());
        blogService.deleteBlog(id);
         if(response.getStatusCode() != HttpStatus.OK){
             redirectAttributes.addFlashAttribute("errorMessage",  "Failed to delete Blog");
         }
         redirectAttributes.addFlashAttribute("successMessage", "Blog Delete Successfully");
         return "redirect:/admin/blog-admin.html";
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