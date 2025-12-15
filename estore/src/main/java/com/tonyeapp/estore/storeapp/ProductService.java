package com.tonyeapp.estore.storeapp;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tonyeapp.estore.accounts.UserInfo;
import com.tonyeapp.estore.accounts.UserInfoRepository;
import com.tonyeapp.estore.service.ImageUploadService;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ImageUploadService imageUploadService;
    private final UserInfoRepository userInfoRepository;

    public ProductService(ProductRepository productRepository, ImageUploadService imageUploadService, UserInfoRepository userInfoRepository) {
        this.productRepository = productRepository;
        this.imageUploadService = imageUploadService;
        this.userInfoRepository = userInfoRepository;
    }

    public Product createProduct(ProductDTO productdto) throws Exception {
        // Get the authenticated user's email
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication(); 
        String userEmail = auth.getName();

        UserInfo user = userInfoRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Product newProduct = new Product();
        newProduct.setDescription(productdto.getDescription());
        newProduct.setPrice(productdto.getPrice());
        newProduct.setStock(productdto.getStock());
        newProduct.setCategory(productdto.getCategory());
        newProduct.setUserInfo(user);
        // upload image first
        String imageUrl = imageUploadService.uploadImage(productdto.getImageUrl());
        newProduct.setImageUrl(imageUrl);
        
        Product saveProduct = productRepository.save(newProduct);
        return saveProduct;
    }   
    

    // update a product
    public Product updateProduct(long id, ProductDTO productdto) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        UserInfo user = userInfoRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("Authenticated User not found in DB"));

        Product productToUpdate = productRepository.findById(id)
        .orElseThrow(() ->new EntityNotFoundException("Product not found with ID: " + id));
        
    
       if (productToUpdate.getUserInfo().getId() != user.getId()) {
            throw new AccessDeniedException("You do not own this product");
        }

        productToUpdate.setDescription(productdto.getDescription());
        productToUpdate.setPrice(productdto.getPrice());
        productToUpdate.setStock(productdto.getStock());
        productToUpdate.setCategory(productdto.getCategory());
        
        if (productdto.getImageUrl() != null && !productdto.getImageUrl().isEmpty()) {
             String imageUrl = imageUploadService.uploadImage(productdto.getImageUrl());
            productToUpdate.setImageUrl(imageUrl);
        }
        return productRepository.save(productToUpdate);
    }

    // delete a product
    public void deleteProduct(long id) throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        UserInfo user = userInfoRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("Authenticated User not found"));

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Check ownership
        if (product.getUserInfo().getId() != user.getId()) {
            throw new AccessDeniedException("You do not own this product");
        }

        productRepository.delete(product);
    }
    
    // VIEW ALL PRODUCTS(ALL USERS)
    public List<ProductResponseDTO> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(pageable);
        
        return products.map(product -> new ProductResponseDTO(
                product.getId(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.getImageUrl()
            )).toList();
    }

    // get a single product
    public ProductResponseDTO getProductById(long id) throws ProductNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        return new ProductResponseDTO(
            product.getId(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.getImageUrl()
        );
    }

    // Search products by description with pagination
    public Page<ProductResponseDTO> searchProducts(String description, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findByDescriptionContainingIgnoreCase(description, pageable);
        
        return products.map(product -> new ProductResponseDTO(
            product.getId(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getCategory(),
            product.getImageUrl()
        ));
    }
}