package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class SingleOrderResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private Order data;

    public boolean isSuccess() { return success; }
    public Order getData() { return data; }
}
