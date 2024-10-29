package walkonmoon.fashion.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.CartItemDTO;
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

    @PersistenceContext
    private EntityManager entityManager;

    public void mergeLocalStorageCart(int userId, List<CartItemDTO> localCartItems) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        Map<Integer, CartItem> cartItemMap = cartItems.stream().collect(
                Collectors.toMap(CartItem::getProductId, cartItem -> cartItem)
        );

        for (var localCartItemDTO : localCartItems) {
            int productId = Integer.parseInt(localCartItemDTO.getId());
            if (cartItemMap.containsKey(productId)) {
                CartItem cartItem = cartItemMap.get(productId);
                cartItem.setQuantity(cartItem.getQuantity() + localCartItemDTO.getQuantity());
                entityManager.merge(cartItem);
            } else {
                CartItem newCartItem = new CartItem();
                newCartItem.setProductId(productId);
                newCartItem.setQuantity(localCartItemDTO.getQuantity());
                newCartItem.setUserId(userId);
                entityManager.persist(newCartItem);
            }
        }
    }
    public List<CartItemDTO> getCartItemsDTOByUserId(int userId) {
        return transferCartItemToCartItemDTO(cartItemRepository.findByUserId(userId));
    }

    public List<CartItemDTO> transferCartItemToCartItemDTO(List<CartItem> cartItems){
        return cartItems.stream().map(cartItem -> {
            CartItemDTO cartItemDTO = new CartItemDTO();
            Product product = productService.getProductById(cartItem.getProductId());
            cartItemDTO.setId(String.valueOf(cartItem.getProductId()));
            cartItemDTO.setTitle(product.getProduct_name());
            cartItemDTO.setPrice(String.valueOf(product.getPrice()));
            cartItemDTO.setImage(product.getImage_collection_url());
            cartItemDTO.setStock(String.valueOf(product.getStock()));
            cartItemDTO.setQuantity(cartItem.getQuantity());
            return cartItemDTO;
        }).collect(Collectors.toList());
    }
}