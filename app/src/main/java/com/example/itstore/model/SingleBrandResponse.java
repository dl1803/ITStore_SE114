package com.example.itstore.model;

public class SingleBrandResponse {
    private boolean success;
    private Brand data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Brand getData() {
        return data;
    }

    public void setData(Brand data) {
        this.data = data;
    }
}
