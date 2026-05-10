package com.example.itstore.model;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
public class ProductImage implements Serializable{
    private int id;
    @SerializedName("image_url")
    private String imageUrl;
    private boolean isPrimary;

    public ProductImage(int id, String imageUrl, boolean isPrimary) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
    }

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isPrimary() {
        return isPrimary;
    }
}
