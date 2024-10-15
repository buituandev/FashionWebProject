package walkonmoon.fashion.service;

import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.Category;
import walkonmoon.fashion.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getListCategories(){
        return (List<Category>) categoryRepository.findAll();
    }
}
