package com.tonyeapp.estore.orderapp;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDTO checkout(@RequestBody CheckOutRequestDTO request) {
        return orderService.checkout(request);
    }

    @GetMapping("/orders/user")
    public List<OrderResponseDTO> userOrders() {
        return orderService.getUserOrders();
    }

    // get admin orders
    @GetMapping("/admin/orders")
    public List<OrderResponseDTO> allOrders() {
        return orderService.getAllAdminOrders();
    }

    @PutMapping("/admin/orders/{id}/status")
    public void updateStatus( @PathVariable long id, @RequestBody UpdateOrderStatusDTO dto) {
        orderService.updateOrderStatus(id, dto.getStatus());
    }
}
