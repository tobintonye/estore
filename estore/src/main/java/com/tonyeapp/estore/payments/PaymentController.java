package com.tonyeapp.estore.payments;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/pay-order")
     public ResponseEntity<?> payForOrder(@RequestBody OrderPaymentRequestDto request) {
        return ResponseEntity.ok(paymentService.payForOrder(request));
    }

     @GetMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestParam String reference) {
        paymentService.verifyPayment(reference);
        return ResponseEntity.ok("Payment verified successfully");
    }
}
