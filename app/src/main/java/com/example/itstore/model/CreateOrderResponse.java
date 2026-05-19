package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class CreateOrderResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private OrderData data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public OrderData getData() { return data; }

    public static class OrderData {
        @SerializedName("order_id")
        private OrderIdWrapper orderId;

        public OrderIdWrapper getOrderId() { return orderId; }
    }

    public static class OrderIdWrapper {
        @SerializedName("id")
        private int id;

        public int getId() { return id; }
    }
}