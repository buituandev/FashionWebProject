package walkonmoon.fashion.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.el.lang.ELArithmetic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import walkonmoon.fashion.model.Category;
import walkonmoon.fashion.model.Image;
import walkonmoon.fashion.model.Product;
import walkonmoon.fashion.model.User;
import walkonmoon.fashion.repository.UserRepository;
import walkonmoon.fashion.service.CategoryService;
import walkonmoon.fashion.service.ImageService;
import walkonmoon.fashion.service.ProductService;
import walkonmoon.fashion.service.UserService;

import java.net.http.HttpRequest;
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
    private UserRepository userRepository;

    @GetMapping("/")
    public String index(Model model) {
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

        return "index";
    }


    @GetMapping("/index.html")
    public String indexHtml(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        Integer userID= null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    if(!cookie.getValue().equalsIgnoreCase("")) {
                        userID = Integer.parseInt(cookie.getValue());
                    }
                }
            }
        }
        if(userID!=null){
            User user = userService.findUserById(userID);
            model.addAttribute("user", user);
        }else{
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

        return "index";
    }

    @GetMapping("/shop-grid.html")
    public String shopGridHtml(Model model) {
        List<Product> products = productService.getListProducts();
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
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
        return "shop-grid";
    }

    @GetMapping("/shop-grid/{categoryId}")
    public String filterShopGrid(@PathVariable int categoryId, Model model) {
        List<Product> filteredProduct = productService.findByCategoryId(categoryId);
        model.addAttribute("products", filteredProduct);
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);
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
        return "shop-grid";
    }

//    @GetMapping("/product-detail")
//    public String productDetail() {
//        return "single-product";
//    }

    @GetMapping("/single-product/{id}")
    public String singleProduct(Model model, @PathVariable String id) {
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
        return "single-product";
    }

    @GetMapping("/categories")
    public String getCategories(Model model) {
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
        return "layout";
    }

    @GetMapping("/single-product.html")
    public String singleProductHtml(Model model) {
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
        return "single-product";
    }

    @GetMapping("/cart.html")
    public String cartHtml(Model model) {
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
        return "cart";
    }


    @GetMapping("/checkout.html")
    public String checkoutHtml(Model model) {
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
    public String loginHtml(Model model) {
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

        return "login";
    }


    @PostMapping("/login/access")
    public String loginAccount(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               Model model,
                               HttpServletResponse response) {
        User currentUser = userService.getUserByEmail(email);
        if (currentUser == null) {
            return "redirect:/login.html";
        } else {
            if (currentUser.getPassword().equals(password)) {
                addCookie(response, "userID", String.valueOf(currentUser.getId()));
//                addCookie(response, "password", currentUser.getPassword());
//                addCookie(response, "email", email);
//                addCookie(response, "gender", currentUser.getGender());
//                addCookie(response, "dob", String.valueOf(currentUser.getDob()));
//                addCookie(response, "address", currentUser.getAddress());
//                addCookie(response, "phoneNumber", currentUser.getPhone_number());
//                addCookie(response, "province", currentUser.getProvince());
//                model.addAttribute("user",currentUser);
                return "redirect:/index.html";
            }
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response,Model model, HttpSession session) {
        Cookie cookie = new Cookie("userID", null);
        response.addCookie(cookie);
        session.invalidate();
        return "redirect:/index.html";
    }

    private void addCookie(HttpServletResponse response, String name, String value) {
        value = value.trim().replace(" ", "_");
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30 * 12);
        response.addCookie(cookie);
    }

    @GetMapping("/register.html")
    public String registerHtml(Model model) {
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
        return "register";
    }

    @PostMapping("/register/save")
    public String registerSave(@ModelAttribute User newUser, Model model) {
        newUser.setType(0);
        newUser.setIs_deleted(0);
        newUser.setAddress("");
        newUser.setImage("");
        newUser.setProvince("");
        User user = userService.getUserByEmail(newUser.getEmail());
        if (user == null) {
            userService.saveUser(newUser);
            return "redirect:/login.html";
        } else {
            return "redirect:/register.html";
        }
    }


    @GetMapping("/contact.html")
    public String contactHtml(Model model) {
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
    public String aboutHtml(Model model) {
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
    public String blogLeftSidebarHtml(Model model) {
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
    public String blogDetailsHtml(Model model) {
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
    public String myAccountHtml(Model model) {
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
        return "my-account";
    }

    @GetMapping("wishlist.html")
    public String wishlistHtml(Model model) {
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
    public String compareHtml(Model model) {
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