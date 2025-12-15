package com.tonyeapp.estore.orderapp;

import lombok.Data;

@Data
public class OrderItemDTO {
    // Represents each item coming from cart/checkout.

    private long productId;
    private int quantity;
    private double price; // price at checkout time
}