package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class ShipmentFeeRequest {
    @SerializedName("address_id")
    private int addressId;

    public ShipmentFeeRequest(int addressId) {
        this.addressId = addressId;
    }
}
