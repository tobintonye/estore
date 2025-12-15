package com.tonyeapp.estore.payments;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PaystackInitializeRequest {
    // paystack request dto
    private String email;
    private BigDecimal amount; // amount in kobo

    
}
