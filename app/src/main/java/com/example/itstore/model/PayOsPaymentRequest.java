package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class PayOsPaymentRequest {
    @SerializedName("order_id")
    private int orderId;

    public PayOsPaymentRequest(int orderId) {
        this.orderId = orderId;
    }
}