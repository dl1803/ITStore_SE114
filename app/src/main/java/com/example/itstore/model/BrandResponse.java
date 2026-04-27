package com.example.itstore.model;

import java.util.List;

public class BrandResponse {
    private boolean success;
    private List<Brand> data;

    public boolean isSuccess() {
        return success;
    }

    public List<Brand> getData() {
        return data;
    }
}