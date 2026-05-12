package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class AddCartItemRequest {
    @SerializedName("variant_id")
    private int variantId;
    @SerializedName("quantity")
    private int quantity;

    public AddCartItemRequest(int variantId, int quantity) {
        this.variantId = variantId;
        this.quantity = quantity;
    }
}