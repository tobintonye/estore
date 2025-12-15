package com.tonyeapp.estore.cartapp;

import java.nio.file.AccessDeniedException;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tonyeapp.estore.accounts.UserInfo;
import com.tonyeapp.estore.accounts.UserInfoRepository;
import com.tonyeapp.estore.storeapp.Product;
import com.tonyeapp.estore.storeapp.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserInfoRepository userInfoRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserInfoRepository userInfoRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userInfoRepository = userInfoRepository;
    }

    public CartItem addItem(CartItemDTO cartItemDTO) {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication(); 
        String userEmail = auth.getName();

        UserInfo user = userInfoRepository.findByEmail(userEmail) 
        .orElseThrow(() -> new RuntimeException("User not found"));

       // Get product by ID
        Product product = productRepository.findById(cartItemDTO.getProductId())
        .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // Create new cart item
        CartItem cartItem = new CartItem();
        cartItem.setUserInfo(user);              
        cartItem.setProduct(product);            
        cartItem.setQuantity(cartItemDTO.getQuantity());

        return cartRepository.save(cartItem);
    }

    // update a cart
    public CartItem UpdateCartItem(long cartItemId, CartItemDTO cartItemDTO) throws Exception {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication(); 
        String userEmail = auth.getName();

        UserInfo user = userInfoRepository.findByEmail(userEmail) 
        .orElseThrow(() -> new RuntimeException("User not found"));

       // Get product by ID
        Product product = productRepository.findById(cartItemDTO.getProductId())
        .orElseThrow(() -> new RuntimeException("Product not found"));
        
        CartItem cartItem = cartRepository.findById(cartItemId) 
        .orElseThrow(() -> new RuntimeException("Cart is item not found"));
        
        // ensure ownership
        if(cartItem.getUserInfo().getId() != user.getId()) {
             throw new AccessDeniedException("You do not own this cart");
        }

        // update cart
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemDTO.getQuantity());

        return cartRepository.save(cartItem);
    }

    // remove an item from cart 
    public void deleteItem(long cartItemId) throws Exception{
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication(); 
        String userEmail = auth.getName();

        UserInfo user = userInfoRepository.findByEmail(userEmail) 
        .orElseThrow(() -> new RuntimeException("User not found"));
        
        CartItem cartItem = cartRepository.findById(cartItemId) 
        .orElseThrow(() -> new RuntimeException("Cart is item not found"));
        
        // ensure ownership
        if(cartItem.getUserInfo().getId() != user.getId()) {
             throw new AccessDeniedException("You do not own this cart");
        }
       // delete the item
        cartRepository.deleteById(cartItemId);
    }

    public List<CartItem> getUserCart() {
    // Get authenticated user
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = auth.getName();

    UserInfo user = userInfoRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("User not found"));

    // Return the user's cart items
    return cartRepository.findByUserInfo(user);
}
}
