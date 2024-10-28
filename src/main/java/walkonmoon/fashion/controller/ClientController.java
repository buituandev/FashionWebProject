package walkonmoon.fashion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import walkonmoon.fashion.model.Category;
import walkonmoon.fashion.model.Image;
import walkonmoon.fashion.model.Product;
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

        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }

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
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "index";
    }

    @GetMapping("/shop-grid.html")
    public String shopGridHtml(Model model){
        List<Product> products = productService.getListProducts();
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "shop-grid";
    }
    @GetMapping("/shop-grid/{categoryId}")
    public String filterShopGrid(@PathVariable int categoryId, Model model){
        List<Product> filteredProduct = productService.findByCategoryId(categoryId);
        model.addAttribute("products", filteredProduct);
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "shop-grid";
    }

//    @GetMapping("/product-detail")
//    public String productDetail() {
//        return "single-product";
//    }
    
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
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "single-product";
    }

    @GetMapping("/categories")
    public String getCategories(Model model) {
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "layout";
    }
    @GetMapping("/single-product.html")
    public String singleProductHtml(Model model){
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "single-product";
    }

    @GetMapping("/cart.html")
    public String cartHtml(Model model){
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "cart";
    }


    @GetMapping("/checkout.html")
    public String checkoutHtml(Model model){
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "checkout";
    }


    @GetMapping("/login.html")
    public String loginHtml(Model model){
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "login";
    }


    @GetMapping("/register.html")
    public String registerHtml(Model model){
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "register";
    }


    @GetMapping("/contact.html")
    public String contactHtml(Model model){
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "contact";
    }

//    @GetMapping("/about")
//    public String about() {
//        return "about";
//    }

    @GetMapping("/about.html")
    public String aboutHtml(Model model){
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "about";
    }

    @GetMapping("/blog-right-sidebar.html")
    public String blogLeftSidebarHtml(Model model){
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "blog-right-sidebar";
    }

    @GetMapping("/blog-details.html")
    public String blogDetailsHtml(Model model){
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "blog-details";
    }


    @GetMapping("/my-account.html")
    public String myAccountHtml(Model model){
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "my-account";
    }

    @GetMapping("wishlist.html")
    public String wishlistHtml(Model model){
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "wishlist";}

    @GetMapping("compare.html")
    public String compareHtml(Model model){
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        System.out.println("Total categories: " + categories.size());
        System.out.println("Chunk size: " + chunkSize);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
                }
            }
        }
        return "compare";}

    @GetMapping("/upload.html")
    public String uploadHtml(){
        return "upload";
    }

}