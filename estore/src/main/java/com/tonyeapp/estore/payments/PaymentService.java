package com.tonyeapp.estore.payments;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.tonyeapp.estore.orderapp.OrderRepository;
import com.tonyeapp.estore.orderapp.OrderStatus;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.tonyeapp.estore.orderapp.Order;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final WebClient webClient;
    private final OrderRepository orderRepository;
    
    @Value("${paystack.secret.key}")
    private String secretKey;

    @Value("${paystack.base.url}")
    private String baseUrl;

    public PaystackInitializeResponse payForOrder(OrderPaymentRequestDto dto) {

        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.PAID) {
            throw new RuntimeException("Order already paid");
        }

        PaystackInitializeRequest request = new PaystackInitializeRequest();
        request.setEmail(dto.getEmail());
        request.setAmount(order.getTotalAmount().multiply(BigDecimal.valueOf(100))); // convert to kobo

        return webClient.post()
        .uri(baseUrl + "/transaction/initialize")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .retrieve()
        .bodyToMono(PaystackInitializeResponse.class) 
        .block(); 
    }

    public void verifyPayment(String reference) {

    PaystackVerifyResponseDto response = webClient.get()
            .uri(baseUrl + "/transaction/verify/" + reference)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
            .retrieve()
            .bodyToMono(PaystackVerifyResponseDto.class)
            .block();

    if (response == null || !response.isStatus()) {
        throw new RuntimeException("Payment verification failed");
    }

    if (!"success".equals(response.getData().getStatus())) {
        throw new RuntimeException("Payment not successful");
    }

    Order order = orderRepository.findByPaymentReference(reference)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    order.setStatus(OrderStatus.PAID);
    orderRepository.save(order);
}
}   
