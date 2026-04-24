package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class OrderItemRequest {
    @SerializedName("variant_id")
    private int variantId;

    @SerializedName("quantity")
    private int quantity;

    public OrderItemRequest(int variantId, int quantity) {
        this.variantId = variantId;
        this.quantity = quantity;
    }

}