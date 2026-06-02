package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class AddWishlistRequest {
    @SerializedName("product_id")
    private int productId;

    public AddWishlistRequest(int productId) {
        this.productId = productId;
    }
}