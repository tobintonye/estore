package com.tonyeapp.estore.orderapp;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tonyeapp.estore.accounts.UserInfo;
import com.tonyeapp.estore.accounts.UserInfoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {
   
    private final OrderRepository orderRepository;
    private final UserInfoRepository userRepository;

    public OrderService(OrderRepository orderRepository, UserInfoRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }
    
    // orders checkout
    public OrderResponseDTO checkout(CheckOutRequestDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        UserInfo user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));
        
        Order order = new Order();
        order.setUserInfo(user);
        order.setStatus(OrderStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;

        for(OrderItemDTO dto : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(dto.getProductId());
            item.setQuantity(dto.getQuantity());
            item.setPrice(dto.getPrice());
            total = total.add(
                BigDecimal.valueOf(dto.getPrice()).multiply(BigDecimal.valueOf(dto.getQuantity()))
            );
            order.getItems().add(item);
        }
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    // Get orders/users
    public List<OrderResponseDTO> getUserOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return orderRepository.findByUserInfoEmail(email)
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

   // Get /admin/orders
    public List<OrderResponseDTO> getAllAdminOrders() {
            return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // update order status /admin/orders/{id}/status
    public void updateOrderStatus(long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
    }

    // Mapper
    private OrderResponseDTO mapToResponse(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());

        dto.setItems(
            order.getItems().stream().map(item -> {
                OrderItemDTO itemDTO = new OrderItemDTO();
                itemDTO.setProductId(item.getProductId());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setPrice(item.getPrice());
                return itemDTO;
            }).toList()
        );

        return dto;
    }
}