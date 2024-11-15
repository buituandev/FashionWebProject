package walkonmoon.fashion.controller;

import com.google.cloud.storage.Acl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
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
    @Autowired
    private EmailService emailService;

    @RequestMapping(value = {"/", "/index.html"})
    public String index(Model model, HttpServletRequest request, HttpSession session) {
        mainAction(request, model, session);
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
    public String shopGridHtml(@RequestParam(defaultValue = "1") int page, Model model, HttpServletRequest request, HttpSession session) {
        mainAction(request, model, session);

        // Fetch products and categories
//        List<Product> products = setModelProductList(model);
        List<Product> products = productService.getListProducts();
        setModelCategoryList(model);
        // Pagination logic
        int pageSize = 8; // Number of products per page
        int totalProducts = products.size();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalProducts);

        // Sublist for the current page
        model.addAttribute("totalProducts", totalProducts);
        List<Product> paginatedProducts = products.subList(start, end);
        model.addAttribute("products", paginatedProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        // Existing logic for cart items
        return "shop-grid";
    }


    @GetMapping("/shop-grid/{categoryId}")
    public String filterShopGrid(@PathVariable int categoryId, @RequestParam(defaultValue = "1") int page, Model model, HttpServletRequest request, HttpSession session) {
        mainAction(request, model, session);

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
    public String singleProduct(Model model, @PathVariable String id, HttpServletRequest request, HttpSession session) {
        var random = new Random();
        mainAction(request, model, session);

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
    public String getCategories(Model model, HttpServletRequest request, HttpSession session) {
        mainAction(request, model, session);
        return "layout";
    }

    @GetMapping("/forgot-password.html")
    public String forgotPassword(Model model, HttpServletRequest request, HttpSession session) {
        mainAction(request, model, session);
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/index.html";
        }
        return "forgot-password";
    }

    @PostMapping("/recover-password")
    public String recoverPassword(@RequestParam("email") String email, Model model, HttpServletRequest request, HttpSession session) {
        mainAction(request, model, session);
        String token = userService.createPasswordResetToken(email);
        if (token != null) {
            session.setAttribute("token", token);
            emailService.sendPasswordRecoveryEmail(email, token);
            model.addAttribute("email", email);
            return "redirect:/announce-email-success.html?email=" + email;
        } else {
            return "forgot-password";
        }
    }

    @GetMapping("/announce-email-success.html")
    public String showAnnounceForm(@RequestParam("email") String email, HttpSession session, HttpServletRequest request, Model model) {
        mainAction(request, model, session);
        System.out.println("Received email: " + email);  // This will log the email
        model.addAttribute("email", email);
        return "announce-email-success";
    }

    @GetMapping("/reset-password.html")
    public String showResetPasswordForm(@RequestParam("token") String token, HttpSession session, HttpServletRequest request, Model model) {
        mainAction(request, model, session);
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password/access")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("newPassword") String newPassword,
                                Model model, HttpServletRequest request, HttpSession session) {
        mainAction(request, model, session);
        // find user by the token
        User user = userService.getUserByToken(token);
        if (user != null && user.getToken() != null
                && user.getToken().equals(token)
                && (user.getTokenExpired().isAfter(LocalDateTime.now())
                && user.getTokenExpired().isBefore(LocalDateTime.now().plusHours(1)))) {
            user.setPassword(UserService.toSHA1(newPassword));
            userService.saveUser(user);
            return "redirect:/login.html";
        } else {

            return "forgot-password";
        }
    }

    @GetMapping("/cart.html")
    public String cartHtml(Model model, HttpServletRequest request, HttpSession session) {
        mainAction(request, model, session);
        return "cart";
    }


    @GetMapping("/checkout.html")
    public String checkoutHtml(Model model, HttpServletRequest request, HttpSession session) {
        mainAction(request, model, session);
        return "checkout";
    }


    @GetMapping("/login.html")
    public String loginHtml(Model model, HttpServletRequest request, HttpSession session) {
        List<Category> categories = setModelCategoryList(model);
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
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/index.html";
        }
        return "login";
    }

    //    @PostMapping("/editProfile")
//    public String editProfileHtml(Model model, HttpServletRequest request, @ModelAttribute User user) {
//        User check = userService.findUserById(user.getId());
//        if (check != null) {
//            check.setGender(user.getGender());
//            check.setPhone_number(user.getPhone_number());
//            check.setDob(user.getDob());
//            check.setType(0);
//            check.setIs_deleted(0);
//            check.setAddress("");
//            check.setImage("");
//            check.setProvince("");
//        }
//        model.addAttribute("user", check);
//        userService.saveUser(check);
//        return "redirect:/my-account.html";
//    }
    @PostMapping("/editProfile")
    public String editProfileHtml(@ModelAttribute User user, Model model) {
        User existingUser = userService.findUserById(user.getId());
        if (existingUser != null) {
            existingUser.setGender(user.getGender());
            existingUser.setPhone_number(user.getPhone_number());
            existingUser.setDob(user.getDob());
            existingUser.setFull_name(user.getFull_name());
            // Update only necessary fields
            userService.saveUser(existingUser);
            model.addAttribute("user", existingUser);
        }
        return "my-account";
    }

    @PostMapping("/login/access")
    public String loginAccount(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               Model model,
                               HttpServletResponse response, HttpSession session) {
        User currentUser = userService.getUserByEmail(email);

        if (currentUser == null) {
            return "redirect:/login.html?loginSuccess=false"; // User not found
        }

        String encryptedPassword = UserService.toSHA1(password);

        if (currentUser.getPassword().equals(encryptedPassword)) {
            session.setAttribute("user", currentUser);
            model.addAttribute("user", currentUser);
            addCartItemsToModel(currentUser.getId(), model);
            return "redirect:/index.html"; // Successful login
        }

        return "redirect:/login.html?loginSuccess=false"; // Incorrect password
    }


    @GetMapping("/register.html")
    public String registerHtml(Model model, HttpServletRequest request, HttpSession session) {
        model.addAttribute("newUser", new User());
        mainAction(request, model, session);
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
    public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        session.invalidate();
        return "redirect:/index.html";
    }


    @GetMapping("/contact.html")
    public String contactHtml(Model model, HttpServletRequest request, HttpSession session) {
        mainAction(request, model, session);
        return "contact";
    }

    @GetMapping("/about.html")
    public String aboutHtml(Model model, HttpServletRequest request, HttpSession session) {
        mainAction(request, model, session);
        return "about";
    }

//    @GetMapping("/reset-password.html")
//    public String verifyCodetHtml(Model model, HttpServletRequest request, HttpSession session) {
//        mainAction(request, model, session);
//        return "reset-password";
//    }

    @GetMapping("/blog.html")
    public String blogLeftSidebarHtml(Model model, HttpServletRequest request, HttpSession session) {
        mainAction(request, model, session);
        return "blog";
    }

    @GetMapping("/blog-details.html")
    public String blogDetailsHtml(Model model, HttpServletRequest request, HttpSession session) {
        mainAction(request, model, session);
        return "blog-details";
    }


    @GetMapping("/my-account.html")
    public String myAccountHtml(Model model, HttpServletRequest request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
            return "redirect:/login.html";
        }
        addCartItemsToModel(user.getId(), model);
        addCategoriesToModel(model);
        return "my-account";
    }

    public void addUserToModel(User user, Model model) {
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }
    }

    public void addCategoriesToModel(Model model) {
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

    private List<Product> setModelProductList(Model model) {
        List<Product> products = productService.getListProducts();
        model.addAttribute("products", products);
        return products;
    }

    private List<Category> setModelCategoryList(Model model) {
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        return categories;
    }

    public void mainAction(HttpServletRequest request, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        addCategoriesToModel(model);
        if (user == null) {
            addCartItemsToModel(null, model);
            addUserToModel(null, model);
        } else {
            addCartItemsToModel(user.getId(), model);
            addUserToModel(user, model);
        }
        model.addAttribute("requestURI", request.getRequestURI());
    }
}