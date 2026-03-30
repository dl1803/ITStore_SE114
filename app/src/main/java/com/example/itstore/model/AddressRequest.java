package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class AddressRequest {
    private String recipient;

    @SerializedName("phone_number")
    private String phoneNumber;
    private String province;
    private String district;
    private String ward;
    private String street;

    public AddressRequest(String recipient, String phoneNumber, String province, String district, String ward, String street) {
        this.recipient = recipient;
        this.phoneNumber = phoneNumber;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.street = street;
    }

}
