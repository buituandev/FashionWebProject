package walkonmoon.fashion.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import walkonmoon.fashion.dto.CartItemDTO;
import walkonmoon.fashion.model.CartItem;
import walkonmoon.fashion.model.Product;
import walkonmoon.fashion.repository.CartItemRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductService productService;

    @Transactional
    public void addCartItem(CartItemDTO cartItemDTO) {
        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(cartItemDTO.getUserId(), cartItemDTO.getId());

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemDTO.getQuantity());
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProductId(cartItemDTO.getId());
            cartItem.setQuantity(cartItemDTO.getQuantity());
            cartItem.setUserId(cartItemDTO.getUserId());
            cartItemRepository.save(cartItem);
        }
    }
    
    @Transactional
    public List<CartItemDTO> getCartItems(int userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        List<Integer> productIds = cartItems.stream().map(CartItem::getProductId).collect(Collectors.toList());
        Map<Integer, Product> productMap = productService.getProductsByIds(productIds).stream().collect(Collectors.toMap(Product::getId, product -> product));

        return cartItems.stream().map(cartItem -> {
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setId(cartItem.getProductId());
            cartItemDTO.setQuantity(cartItem.getQuantity());
            cartItemDTO.setUserId(cartItem.getUserId());

            Product product = productMap.get(cartItem.getProductId());
            cartItemDTO.setTitle(product.getProduct_name());
            cartItemDTO.setImage(product.getImage_collection_url());
            cartItemDTO.setPrice(product.getPrice());
            cartItemDTO.setStock(product.getStock());

            return cartItemDTO;
        }).collect(Collectors.toList());
    }
    
    @Transactional
    public void deleteCartItem(int userId, int productId) {
        cartItemRepository.deleteByUserIdAndProductId(userId, productId);
    }
    
    @Transactional
    public int calculateTotalPrice(List<CartItemDTO> cartItems) {
        return cartItems.stream().mapToInt(cartItem -> cartItem.getPrice() * cartItem.getQuantity()).sum();
    }
    
}