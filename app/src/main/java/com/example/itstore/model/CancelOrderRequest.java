package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class CancelOrderRequest {
    @SerializedName("cancel_reason")
    private String cancelReason;

    public CancelOrderRequest(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getCancelReason() {
        return cancelReason;
    }
}
