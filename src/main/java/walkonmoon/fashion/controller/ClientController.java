package walkonmoon.fashion.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import walkonmoon.fashion.dto.CartItemDTO;
import org.springframework.web.bind.annotation.*;
import walkonmoon.fashion.model.Category;
import walkonmoon.fashion.model.Image;
import walkonmoon.fashion.model.Product;
import walkonmoon.fashion.model.User;
import walkonmoon.fashion.service.CategoryService;
import walkonmoon.fashion.service.ImageService;
import walkonmoon.fashion.service.ProductService;
import walkonmoon.fashion.service.UserService;
import walkonmoon.fashion.service.*;

import java.util.ArrayList;
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
    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Integer userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if (!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

        List<Product> products = productService.getListProducts();
        model.addAttribute("products", products);

        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        List<Category> topCategories = categories.stream()
                .sorted((c1, c2) -> {
                    int count1 = productService.findByCategoryId(c1.getId()).size();
                    int count2 = productService.findByCategoryId(c2.getId()).size();
                    return Integer.compare(count2, count1); // descending order
                })
                .limit(4)
                .toList();
        model.addAttribute("topCategories", topCategories);

        for (int i = 0; i < topCategories.size(); i++) {
            Category category = topCategories.get(i);
            model.addAttribute("category" + (i + 1), category);
            List<Product> filteredProducts = productService.findByCategoryId(category.getId());
            model.addAttribute("topCategoryProducts" + (i + 1), filteredProducts);
        }

        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                }
            }
        }

        List<CartItemDTO> cartItems = new ArrayList<>();
        String userId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userID".equals(cookie.getName())) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }

        if (!(userId == null)) {
            cartItems = cartItemService.getCartItems(Integer.parseInt(userId));
        }

        int totalPrice = cartItemService.calculateTotalPrice(cartItems);
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("cartItems", cartItems);

        return "index";
    }


    @GetMapping("/index.html")
    public String indexHtml(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        Integer userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if (!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

        List<Product> products = productService.getListProducts();
        model.addAttribute("products", products);

        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        List<Category> topCategories = categories.stream()
                .sorted((c1, c2) -> {
                    int count1 = productService.findByCategoryId(c1.getId()).size();
                    int count2 = productService.findByCategoryId(c2.getId()).size();
                    return Integer.compare(count2, count1); // descending order
                })
                .limit(4)
                .toList();
        model.addAttribute("topCategories", topCategories);

        for (int i = 0; i < topCategories.size(); i++) {
            Category category = topCategories.get(i);
            model.addAttribute("category" + (i + 1), category);
            List<Product> filteredProducts = productService.findByCategoryId(category.getId());
            model.addAttribute("topCategoryProducts" + (i + 1), filteredProducts);
        }

        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                }
            }
        }

        List<CartItemDTO> cartItems = new ArrayList<>();
        String userId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userID".equals(cookie.getName())) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }

        if (!(userId == null)) {
            cartItems = cartItemService.getCartItems(Integer.parseInt(userId));
        }

        int totalPrice = cartItemService.calculateTotalPrice(cartItems);
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("cartItems", cartItems);

        return "index";
    }

//    @GetMapping("/shop-grid.html")
//    public String shopGridHtml(Model model, HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        Integer userID = null;
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("userID")) {
//                    if (!cookie.getValue().equalsIgnoreCase("")) {
//                        userID = Integer.parseInt(cookie.getValue());
//                    }
//                }
//            }
//        }
//        if (userID != null) {
//            User user = userService.findUserById(userID);
//            model.addAttribute("user", user);
//        } else {
//            model.addAttribute("user", null);
//        }
//
//        List<Product> products = productService.getListProducts();
//        List<Category> categories = categoryService.getListCategories();
//        model.addAttribute("products", products);
//        model.addAttribute("categories", categories);
//        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
//
//        for (int i = 0; i < 4; i++) {
//            int start = i * chunkSize;
//            int end = Math.min(start + chunkSize, categories.size());
//
//            if (start < categories.size()) {
//                if (i == 3) {
//                    List<Category> chunk = categories.subList(start, categories.size());
//                    model.addAttribute("categories" + (i + 1), chunk);
//                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
//                } else {
//                    List<Category> chunk = categories.subList(start, end);
//                    model.addAttribute("categories" + (i + 1), chunk);
//                    System.out.println("Chunk " + (i + 1) + " size: " + chunk.size());
//                }
//            }
//        }
//        List<CartItemDTO> cartItems = new ArrayList<>();
//        String userId = null;
//
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("userID".equals(cookie.getName())) {
//                    userId = cookie.getValue();
//                    break;
//                }
//            }
//        }
//
//        if (!(userId == null)) {
//            cartItems = cartItemService.getCartItems(Integer.parseInt(userId));
//        }
//
//        int totalPrice = cartItemService.calculateTotalPrice(cartItems);
//        model.addAttribute("totalPrice", totalPrice);
//
//        model.addAttribute("cartItems", cartItems);
//        return "shop-grid";
//    }
@GetMapping("/shop-grid.html")
public String shopGridHtml(@RequestParam(defaultValue = "1") int page, Model model, HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    Integer userID = null;

    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("userID")) {
                if (!cookie.getValue().isEmpty()) {
                    userID = Integer.parseInt(cookie.getValue());
                }
            }
        }
    }

    // Fetch user information if available
    if (userID != null) {
        User user = userService.findUserById(userID);
        model.addAttribute("user", user);
    } else {
        model.addAttribute("user", null);
    }

    // Fetch products and categories
    List<Product> products = productService.getListProducts();
    List<Category> categories = categoryService.getListCategories();

    model.addAttribute("categories", categories);
    // chunk categories
    int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks

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

    // Pagination logic
    int pageSize = 8; // Number of products per page
    int totalProducts = products.size();
    int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
    int start = (page - 1) * pageSize;
    int end = Math.min(start + pageSize, totalProducts);

    // Sublist for the current page
    List<Product> paginatedProducts = products.subList(start, end);
    model.addAttribute("products", paginatedProducts);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", totalPages);

    // Existing logic for cart items
    List<CartItemDTO> cartItems = new ArrayList<>();
    String userId = null;

    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("userID".equals(cookie.getName())) {
                userId = cookie.getValue();
                break;
            }
        }
    }

    if (userId != null) {
        cartItems = cartItemService.getCartItems(Integer.parseInt(userId));
    }

    int totalPrice = cartItemService.calculateTotalPrice(cartItems);
    model.addAttribute("totalPrice", totalPrice);
    model.addAttribute("cartItems", cartItems);

    return "shop-grid";
}



    @GetMapping("/shop-grid/{categoryId}")
    public String filterShopGrid(@PathVariable int categoryId, @RequestParam(defaultValue = "1") int page, Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Integer userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if (!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

        List<Product> filteredProduct = productService.findByCategoryId(categoryId);
        model.addAttribute("products", filteredProduct);
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        //chunk categries
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                }
            }
        }
        // Pagination logic
        int pageSize = 8; // Number of products per page
        int totalProducts = filteredProduct.size();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalProducts);

        // Sublist for the current page
        List<Product> paginatedProducts = filteredProduct.subList(start, end);
        model.addAttribute("products", paginatedProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        // Existing logic for cart items
        model.addAttribute("selectedCategoryId", categoryId);
        // cart
        List<CartItemDTO> cartItems = new ArrayList<>();
        String userId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userID".equals(cookie.getName())) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }

        if (!(userId == null)) {
            cartItems = cartItemService.getCartItems(Integer.parseInt(userId));
        }

        int totalPrice = cartItemService.calculateTotalPrice(cartItems);
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("cartItems", cartItems);
        return "shop-grid";
    }

    @GetMapping("/single-product/{id}")
    public String singleProduct(Model model, @PathVariable String id, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Integer userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if (!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

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
        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                }
            }
        }
        List<CartItemDTO> cartItems = new ArrayList<>();
//        Cookie[] cookies = request.getCookies();
        String userId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userID".equals(cookie.getName())) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }

        if (!(userId == null)) {
            cartItems = cartItemService.getCartItems(Integer.parseInt(userId));
        }

        int totalPrice = cartItemService.calculateTotalPrice(cartItems);
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("cartItems", cartItems);
        return "single-product";
    }

    @GetMapping("/categories")
    public String getCategories(Model model, HttpServletRequest request) {
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                }
            }
        }
        List<CartItemDTO> cartItems = new ArrayList<>();
        Cookie[] cookies = request.getCookies();
        String userId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userID".equals(cookie.getName())) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }

        if (!(userId == null)) {
            cartItems = cartItemService.getCartItems(Integer.parseInt(userId));
        }

        int totalPrice = cartItemService.calculateTotalPrice(cartItems);
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("cartItems", cartItems);
        return "layout";
    }

    @GetMapping("/single-product.html")
    public String singleProductHtml(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Integer userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if (!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                }
            }
        }
        List<CartItemDTO> cartItems = new ArrayList<>();
        String userId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userID".equals(cookie.getName())) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }

        if (!(userId == null)) {
            cartItems = cartItemService.getCartItems(Integer.parseInt(userId));
        }

        int totalPrice = cartItemService.calculateTotalPrice(cartItems);
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("cartItems", cartItems);
        return "single-product";
    }

    @GetMapping("/cart.html")
    public String cartHtml(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Integer userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if (!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                }
            }
        }

        List<CartItemDTO> cartItems = new ArrayList<>();
        String userId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userID".equals(cookie.getName())) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }

        if (!(userId == null)) {
            cartItems = cartItemService.getCartItems(Integer.parseInt(userId));
        }

        int totalPrice = cartItemService.calculateTotalPrice(cartItems);
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("cartItems", cartItems);
        return "cart";
    }


    @GetMapping("/checkout.html")
    public String checkoutHtml(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Integer userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if (!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                }
            }
        }
        return "checkout";
    }


    @GetMapping("/login.html")
    public String loginHtml(Model model, HttpServletRequest request) {
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                }
            }
        }

        List<CartItemDTO> cartItems = new ArrayList<>();
        Cookie[] cookies = request.getCookies();
        String userId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userID".equals(cookie.getName()) && !cookie.getValue().equalsIgnoreCase("")) {
                    userId = cookie.getValue();
                }
            }
        }

        if (!(userId == null)) {
            return "redirect:/index.html";
        }
        return "login";
    }

    @PostMapping("/editProfile")
    public String editProfileHtml(Model model, HttpServletRequest request, @ModelAttribute User user) {
        User check = userService.findUserById(user.getId());
        if (check != null) {
            check.setGender(user.getGender());
            check.setPhone_number(user.getPhone_number());
            check.setDob(user.getDob());
            check.setType(0);
            check.setIs_deleted(0);
            check.setAddress("");
            check.setImage("");
            check.setProvince("");
        }
        model.addAttribute("user", check);
        userService.saveUser(check);
        return "redirect:/my-account.html";
    }


    @PostMapping("/login/access")
    public String loginAccount(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               Model model,
                               HttpServletResponse response) {
        User currentUser = userService.getUserByEmail(email);

        // Check if the user exists
        if (currentUser == null) {
            return "redirect:/login.html?loginSuccess=false"; // User not found
        }

        String encryptedPassword = UserService.toSHA1(password);

        // Check if the password matches
        if (currentUser.getPassword().equals(encryptedPassword)) {
            addCookie(response, "userID", String.valueOf(currentUser.getId()));
            return "redirect:/index.html"; // Successful login
        }

        // If password does not match, redirect with failure
        return "redirect:/login.html?loginSuccess=false"; // Incorrect password
    }


    private void addCookie(HttpServletResponse response, String name, String value) {
        value = value.trim().replace(" ", "_");
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30 * 12);
        response.addCookie(cookie);
    }

    @GetMapping("/register.html")
    public String registerHtml(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Integer userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if (!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);

                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);

                }
            }
        }
        model.addAttribute("newUser", new User());

        List<CartItemDTO> cartItems = new ArrayList<>();
//        Cookie[] cookies = request.getCookies();
        String userId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userID".equals(cookie.getName())) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }

        if (!(userId == null)) {
            cartItems = cartItemService.getCartItems(Integer.parseInt(userId));
        }

        int totalPrice = cartItemService.calculateTotalPrice(cartItems);
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("cartItems", cartItems);

        return "register";
    }

    @PostMapping("/register/save")
    public String registerSave(@ModelAttribute User newUser, Model model) {
        newUser.setType(0);
        newUser.setIs_deleted(0);
        newUser.setAddress("");
        newUser.setImage("");
        newUser.setProvince("");
        //encrypt password
        String encryptedPassword = UserService.toSHA1(newUser.getPassword());
        newUser.setPassword(encryptedPassword);
        User user = userService.getUserByEmail(newUser.getEmail());
        // end of encrypt password
        if (user == null) {
            userService.saveUser(newUser);
            return "redirect:/login.html?registerSuccess=true";
        } else {
            return "redirect:/register.html?registerSuccess=false";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("userID".equalsIgnoreCase(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        return "redirect:/index.html";
    }


    @GetMapping("/contact.html")
    public String contactHtml(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Integer userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if (!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);

                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);

                }
            }
        }
        return "contact";
    }

    @GetMapping("/about.html")
    public String aboutHtml(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Integer userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if (!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);

                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);

                }
            }
        }
        return "about";
    }

    @GetMapping("/blog-right-sidebar.html")
    public String blogLeftSidebarHtml(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Integer userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if (!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);

                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                }
            }
        }
        return "blog-right-sidebar";
    }

    @GetMapping("/blog-details.html")
    public String blogDetailsHtml(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Integer userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if (!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                }
            }
        }
        return "blog-details";
    }


    @GetMapping("/my-account.html")
    public String myAccountHtml(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Integer userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if (!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
            return "redirect:/login.html";
        }

        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                }
            }
        }
        return "my-account";
    }

    @GetMapping("wishlist.html")
    public String wishlistHtml(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Integer userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if (!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                }
            }
        }
        return "wishlist";
    }

    @GetMapping("compare.html")
    public String compareHtml(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Integer userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if (!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / (double) 4); // Calculate how many chunks
        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                if (i == 3) {
                    List<Category> chunk = categories.subList(start, categories.size());
                    model.addAttribute("categories" + (i + 1), chunk);
                } else {
                    List<Category> chunk = categories.subList(start, end);
                    model.addAttribute("categories" + (i + 1), chunk);
                }
            }
        }
        return "compare";
    }

    @GetMapping("/upload.html")
    public String uploadHtml() {
        return "upload";
    }

}