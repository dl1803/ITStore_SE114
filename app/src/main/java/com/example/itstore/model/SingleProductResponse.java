package com.example.itstore.model;

public class SingleProductResponse {
    private boolean success;
    private Product data;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public Product getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

}
