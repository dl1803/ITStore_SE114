package com.example.itstore.model;
import java.util.List;

public class BannerResponse {
    private boolean success;
    private List<Banner> data;

    public boolean isSuccess() { return success; }
    public List<Banner> getData() { return data; }
}