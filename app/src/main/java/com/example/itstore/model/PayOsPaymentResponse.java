package com.example.itstore.model;

public class PayOsPaymentResponse {
    private boolean success;
    private PaymentData data;
    private String message;

    public boolean isSuccess() { return success; }
    public PaymentData getData() { return data; }
    public String getMessage() { return message; }

    public static class PaymentData {
        private int payment_id;
        private int order_id;
        private String paymentUrl;

        public int getPaymentId() { return payment_id; }
        public int getOrderId() { return order_id; }
        public String getPaymentUrl() { return paymentUrl; }
    }
}