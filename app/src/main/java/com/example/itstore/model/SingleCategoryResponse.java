package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class SingleCategoryResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private Category data;
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public Category getData() {
        return data;
    }
    public void setData(Category data) {
        this.data = data;
    }
}
