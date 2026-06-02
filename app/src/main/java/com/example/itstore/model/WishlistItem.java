package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class WishlistItem {
    @SerializedName("id")
    private int id;

    @SerializedName("added_at")
    private String addedAt;

    @SerializedName("product")
    private Product product;

    public int getId() { return id; }
    public String getAddedAt() { return addedAt; }
    public Product getProduct() { return product; }
}