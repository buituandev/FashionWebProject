package walkonmoon.fashion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import walkonmoon.fashion.model.Category;
import walkonmoon.fashion.service.CategoryService;

import java.util.List;

@Controller
public class CustomErrorController implements ErrorController {

    @Autowired
    CategoryService categoryService;
    @RequestMapping("/error")
    public String handleError(Model model) {
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories", categories);
        return "404";
    }
}