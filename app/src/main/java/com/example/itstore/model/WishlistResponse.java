package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WishlistResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private List<WishlistItem> data;

    public boolean isSuccess() { return success; }
    public List<WishlistItem> getData() { return data; }
}