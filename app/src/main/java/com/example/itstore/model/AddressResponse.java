package com.example.itstore.model;

import java.util.List;

public class AddressResponse {
    private boolean success;
    private String message;
    private List<Address> data;

    public boolean isSucess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Address> getData() {
        return data;
    }

}
