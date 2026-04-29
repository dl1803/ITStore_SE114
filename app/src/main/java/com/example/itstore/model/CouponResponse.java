package com.example.itstore.model;

import java.util.List;

public class CouponResponse {
    private boolean success;
    private List<Coupon> data;

    public boolean isSuccess() {
        return success;
    }

    public List<Coupon> getData() {
        return data;
    }
}
