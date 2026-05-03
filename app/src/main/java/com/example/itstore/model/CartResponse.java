package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CartResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private List<CartItem> data;

    public boolean isSuccess() {
        return success;
    }

    public List<CartItem> getData() {
        return data;
    }
}