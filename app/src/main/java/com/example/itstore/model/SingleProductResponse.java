package com.example.itstore.model;

public class SingleProductResponse {
    private boolean success;
    private Product data;

    public boolean isSuccess() {
        return success;
    }

    public Product getData() {
        return data;
    }
}
