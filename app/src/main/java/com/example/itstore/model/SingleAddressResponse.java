package com.example.itstore.model;

public class SingleAddressResponse {
    private boolean success;
    private String message;
    private Address data;

    public boolean isSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }
    public Address getData() {
        return data;
    }
}
