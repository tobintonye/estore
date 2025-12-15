package com.tonyeapp.estore.orderapp;

import java.util.List;
import lombok.Data;


@Data
public class CheckOutRequestDTO {
    // Used when a user places an order.
    List<OrderItemDTO> items;
}
