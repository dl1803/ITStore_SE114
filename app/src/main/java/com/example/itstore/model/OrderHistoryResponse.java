package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrderHistoryResponse {

    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private List<Order> data;

    public boolean isSuccess() { return success; }

    public List<Order> getData() { return data; }
}