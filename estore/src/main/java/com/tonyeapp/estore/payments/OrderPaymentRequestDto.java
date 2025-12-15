package com.tonyeapp.estore.payments;

import lombok.Data;
@Data
public class OrderPaymentRequestDto {
    private long orderId;
    private String email;
}
