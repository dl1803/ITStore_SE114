package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class ReturnRequestResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ReturnRequestData data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public ReturnRequestData getData() { return data; }

    public static class ReturnRequestData {
        @SerializedName("id")
        private int id;

        @SerializedName("status")
        private String status;

        @SerializedName("refund_amount")
        private double refundAmount;

        @SerializedName("created_at")
        private String createdAt;

        public int getId() { return id; }
        public String getStatus() { return status; }
        public double getRefundAmount() { return refundAmount; }
        public String getCreatedAt() { return createdAt; }
    }
}