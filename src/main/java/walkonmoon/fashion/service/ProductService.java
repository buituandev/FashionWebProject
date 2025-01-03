package walkonmoon.fashion.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.Product;
import walkonmoon.fashion.model.ProductStatus;
import walkonmoon.fashion.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    public Product getProductById(int id){
        Optional<Product> product = proRepo.findById(id);
        return product.orElse(null);
    }
    @Transactional
    public List<Product> findByCategoryId(int categoryID){
        List<Product> temps = proRepo.findAll();
        List<Product> products = new ArrayList<Product>();
        for(Product product : temps){
            if(product.getStatus()== ProductStatus.ENABLE && product.getCategoryId() == categoryID){
                products.add(product);
            }
        }
        return products;
    }

    @Transactional
    public void deleteProductById(int id){
        proRepo.deleteById(id);
    }
    
    @Transactional
    public List<Product> getProductsByIds(List<Integer> productIds) {
        return proRepo.findByIdIn(productIds);
    }

    @Transactional
    public void deleteProducts(List<Integer> productIds) {
        try {
            for(Integer id : productIds){
                proRepo.deleteById(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
