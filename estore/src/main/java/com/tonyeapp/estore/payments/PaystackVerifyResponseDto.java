package com.tonyeapp.estore.payments;

import lombok.Data;

@Data
public class PaystackVerifyResponseDto {

    private boolean status;
    private String message;
    private DataPayload data;

    @Data
    public static class DataPayload {
        private String status;   // success, failed
        private String reference;
        private int amount;
    }
}
