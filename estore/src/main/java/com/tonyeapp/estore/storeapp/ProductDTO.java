package com.tonyeapp.estore.storeapp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @Positive(message = "Price must be a positive number")
    private double price; 
    
    @Positive(message = "Stock must be a positive number")
    private int stock;

    @NotBlank(message = "Category cannot be empty")
    private String category;
    
    private MultipartFile imageUrl;
}
