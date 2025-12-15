package com.tonyeapp.estore.storeapp;


import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {
  
    private final ProductService service;

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size ) {
        List<ProductResponseDTO> products = service.getAllProducts(page, size);
        return ResponseEntity.ok(products);
    }

    // get a single product
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable long id) throws ProductNotFoundException {
        ProductResponseDTO product = service.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // Search products by description
    @GetMapping("/products/search")
    public ResponseEntity<Page<ProductResponseDTO>> searchProducts(
        @RequestParam String description,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<ProductResponseDTO> products = service.searchProducts(description, page, size);
        return ResponseEntity.ok(products);
    }

    // only admins can make, post, put and delete requests 
    @PostMapping(value = "/admin/products/", consumes = "multipart/form-data")
    public ResponseEntity<String> addProduct(@Valid @ModelAttribute ProductDTO productdto) throws Exception {
        service.createProduct(productdto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
    }

    // update product
    @PutMapping(value = "/admin/products/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Product> editProduct(@PathVariable long id, @Valid @ModelAttribute ProductDTO productdto ) throws Exception {
         Product updatedProduct = service.updateProduct(id, productdto);
        return ResponseEntity.ok(updatedProduct);
    }

    // delete product
    @DeleteMapping("/admin/products/{id}")
    public String delete(@PathVariable long id) throws AccessDeniedException   {
        service.deleteProduct(id);
        return "Product deleted";
    }

}
