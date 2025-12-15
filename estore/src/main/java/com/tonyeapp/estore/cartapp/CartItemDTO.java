package com.tonyeapp.estore.cartapp;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private long productId;
    @Positive(message = "Quantity must be a positive number")
    private int quantity;
}
