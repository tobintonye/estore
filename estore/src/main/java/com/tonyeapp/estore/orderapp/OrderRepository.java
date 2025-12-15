package com.tonyeapp.estore.orderapp;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserInfoEmail(String email);
    Optional<Order> findByPaymentReference(String paymentReference);
}
