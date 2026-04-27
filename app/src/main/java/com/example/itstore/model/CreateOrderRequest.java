package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CreateOrderRequest {
    @SerializedName("address_id")
    private int addressId;

    // Dùng Integer thay vì int để có thể truyền null nếu khách không dùng mã giảm giá
    @SerializedName("coupon_id")
    private Integer couponId;

    @SerializedName("payment_method")
    private String paymentMethod;

    @SerializedName("note")
    private String note;

    @SerializedName("items")
    private List<OrderItemRequest> items;

    public CreateOrderRequest(int addressId, Integer couponId, String paymentMethod, String note, List<OrderItemRequest> items) {
        this.addressId = addressId;
        this.couponId = couponId;
        this.paymentMethod = paymentMethod;
        this.note = note;
        this.items = items;
    }
}