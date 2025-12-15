package com.tonyeapp.estore.storeapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private long id;
    private String description;
    private double price; 
    private int stock;
    private String category;
    private String imageUrl;
}
