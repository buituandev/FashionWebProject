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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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

    @RequestMapping(value = {"/", "/index.html"})
    public String index(Model model, HttpServletRequest request) {
        mainAction(request, model);

        setModelProductList(model);

        var categories = setModelCategoryList(model);

        Map<Integer, List<Product>> categoryProductsMap = new HashMap<>();
        for (Category category : categories) {
            List<Product> filteredProducts = productService.findByCategoryId(category.getId());
            categoryProductsMap.put(category.getId(), filteredProducts);
        }

        model.addAttribute("categoryProductsMap", categoryProductsMap);
        return "index";
    }

    @RequestMapping(value = {"/shop-grid", "/shop-grid.html"})
    public String shopGridHtml(@RequestParam(defaultValue = "1") int page, Model model, HttpServletRequest request) {
        mainAction(request, model);

        // Fetch products and categories
        List<Product> products = setModelProductList(model);
        setModelCategoryList(model);
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
        return "shop-grid";
    }


    @GetMapping("/shop-grid/{categoryId}")
    public String filterShopGrid(@PathVariable int categoryId, @RequestParam(defaultValue = "1") int page, Model model, HttpServletRequest request) {
        mainAction(request, model);

        List<Product> filteredProduct = productService.findByCategoryId(categoryId);
        model.addAttribute("products", filteredProduct);
        setModelCategoryList(model);
        // Pagination logic
        int pageSize = 8;
        int totalProducts = filteredProduct.size();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalProducts);

        List<Product> paginatedProducts = filteredProduct.subList(start, end);
        model.addAttribute("products", paginatedProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("selectedCategoryId", categoryId);
        return "shop-grid";
    }

    @GetMapping("/single-product/{id}")
    public String singleProduct(Model model, @PathVariable String id, HttpServletRequest request) {
        var random = new Random();
        mainAction(request, model);

        Product product = productService.getProductById(Integer.parseInt(id));
        model.addAttribute("product", product);
        List<Image> productImages = imageService.findByProductId(product.getId());
        model.addAttribute("productImages", productImages);
        List<Product> relatedProducts = productService.findByCategoryId(product.getCategoryId());
        List<Product> filteredProducts = relatedProducts.stream()
                .filter(p -> p.getId() != product.getId())
                .collect(Collectors.toList());
        Collections.shuffle(filteredProducts, random);
        List<Product> limitedProducts = filteredProducts.stream()
                .limit(6)
                .collect(Collectors.toList());
        relatedProducts.clear();
        relatedProducts = limitedProducts;
        model.addAttribute("relatedProducts", relatedProducts);
        return "single-product";
    }

    @GetMapping("/categories")
    public String getCategories(Model model, HttpServletRequest request) {
       mainAction(request, model);
        return "layout";
    }

    @GetMapping("/cart.html")
    public String cartHtml(Model model, HttpServletRequest request) {
        mainAction(request, model);
        return "cart";
    }


    @GetMapping("/checkout.html")
    public String checkoutHtml(Model model, HttpServletRequest request) {
        mainAction(request, model);
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
        Integer userId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userID".equals(cookie.getName()) && !cookie.getValue().equalsIgnoreCase("")) {
                    userId = Integer.parseInt(cookie.getValue());
                }
            }
        }
        if ((userId != null)) {
            model.addAttribute("user", userService.findUserById(userId));
            return "redirect:/index.html";
        } else {
            model.addAttribute("user", null);
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
        mainAction(request, model);
        return "contact";
    }

    @GetMapping("/about.html")
    public String aboutHtml(Model model, HttpServletRequest request) {
        mainAction(request, model);
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
        mainAction(request, model);
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

    private Integer getUserIdFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userID".equals(cookie.getName()) && !cookie.getValue().isEmpty()) {
                    return Integer.parseInt(cookie.getValue());
                }
            }
        }
        return null;
    }

    private void addUserToModel(Integer userID, Model model) {
        if (userID != null) {
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }
    }

    private void addCategoriesToModel(Model model) {
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        int chunkSize = (int) Math.floor((double) categories.size() / 4);

        for (int i = 0; i < 4; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, categories.size());

            if (start < categories.size()) {
                List<Category> chunk = (i == 3) ? categories.subList(start, categories.size()) : categories.subList(start, end);
                model.addAttribute("categories" + (i + 1), chunk);
            }
        }
    }

    private void addCartItemsToModel(Integer userID, Model model) {
        List<CartItemDTO> cartItems = new ArrayList<>();
        if (userID != null) {
            cartItems = cartItemService.getCartItems(userID);
        }
        int totalPrice = cartItemService.calculateTotalPrice(cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cartItems", cartItems);
    }

    private List<Product> setModelProductList(Model model){
        List<Product> products = productService.getListProducts();
        model.addAttribute("products", products);
        return products;
    }

    private List<Category> setModelCategoryList(Model model) {
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        return categories;
    }

    private void mainAction(HttpServletRequest request, Model model) {
        Integer userID = getUserIdFromCookies(request);
        addUserToModel(userID, model);
        addCategoriesToModel(model);
        addCartItemsToModel(userID, model);
    }
}