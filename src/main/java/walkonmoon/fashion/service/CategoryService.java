package walkonmoon.fashion.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.Category;
import walkonmoon.fashion.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getListCategories(){
        return (List<Category>) categoryRepository.findAll();
    }

    @Transactional
    public void saveCategory(Category category){
        categoryRepository.save(category);
    }

    @Transactional
    public Category getCategoryById(int id){
        Optional<Category> categoryID = categoryRepository.findById(id);
        return categoryID.orElse(null);
    }
}
