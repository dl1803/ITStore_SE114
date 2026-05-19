package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class PayOsPaymentResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private MoMoData data;

    public boolean isSuccess() { return success; }
    public MoMoData getData() { return data; }
    public String getMessage() { return message; }


    public static class MoMoData {
        @SerializedName("payUrl")
        private String payUrl;

        public String getPayUrl() { return payUrl; }
    }
}
