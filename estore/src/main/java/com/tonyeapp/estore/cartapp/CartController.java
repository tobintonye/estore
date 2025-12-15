package com.tonyeapp.estore.cartapp;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;

    // add to cart
    @PostMapping("/cart/add")
    public ResponseEntity<CartItem> AddToCart(@Valid @RequestBody CartItemDTO cartItemDTO) {
        CartItem savedItem = cartService.addItem(cartItemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }

    // update cart
    @PutMapping("/cart/update/{id}")
    public ResponseEntity<CartItem> updateCart(@PathVariable("id") long cartItemId, @Valid @RequestBody CartItemDTO cartItemDTO) throws Exception {
        CartItem updatedItem = cartService.UpdateCartItem(cartItemId, cartItemDTO);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/cart/remove/{id}")
    public ResponseEntity<String> deleteFromCart(@PathVariable("id") long cartItemId) throws Exception {
        cartService.deleteItem(cartItemId);
        return ResponseEntity.ok("Cart item deleted"); 
    }

    @GetMapping("/cart")
    public ResponseEntity<List<CartItem>> getMyCart() {
        List<CartItem> items = cartService.getUserCart();
        return ResponseEntity.ok(items);
    }
}
