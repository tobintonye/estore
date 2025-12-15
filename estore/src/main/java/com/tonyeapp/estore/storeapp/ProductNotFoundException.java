package com.tonyeapp.estore.storeapp;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String message) {
        super(message);    
    }
    
     public ProductNotFoundException(long id) {
        super("Product with ID " + id + " not found."); // Creates a default message
    }
}
