package com.tonyeapp.estore.orderapp;

import lombok.Data;

@Data
public class UpdateOrderStatusDTO {
    // used by admin 
    private OrderStatus status;
}
