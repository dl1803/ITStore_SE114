package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class Coupon {
    private int id;
    private String code;

    @SerializedName("discount_type")
    private String discountType;
    @SerializedName("discount_value")
    private double discountValue;
    @SerializedName("min_order_value")
    private double minOrderValue;
    @SerializedName("expires_at")
    private String expiresAt;

    public int getId() { return id; }
    public String getCode() { return code; }
    public String getDiscountType() { return discountType; }
    public double getDiscountValue() { return discountValue; }
    public double getMinOrderValue() { return minOrderValue; }
    public String getExpiresAt() { return expiresAt; }

}
