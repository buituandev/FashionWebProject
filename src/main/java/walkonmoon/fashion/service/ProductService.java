package walkonmoon.fashion.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.Product;
import walkonmoon.fashion.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository proRepo;

    public ProductService(ProductRepository proRepo) {
        this.proRepo = proRepo;
    }

    public List<Product> getListProducts(){
        return (List<Product>) proRepo.findAll();
    }

    @Transactional
    public void saveProduct(Product product){
        proRepo.save(product);
    }
}
