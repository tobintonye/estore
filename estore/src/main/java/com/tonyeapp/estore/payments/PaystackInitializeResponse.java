package com.tonyeapp.estore.payments;

import lombok.Data;

@Data
public class PaystackInitializeResponse {
    // paystack response
    private boolean status;
    private String message;
    private DataPayload data;

    @Data
    public static class DataPayload {
        private String authorization_url;
        private String access_code;
        private String reference;
    }
}
