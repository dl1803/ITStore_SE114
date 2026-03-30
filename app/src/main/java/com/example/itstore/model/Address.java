package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class Address {
    private int id;
    @SerializedName("user_id")
    private int userId;

    private String recipient;
    @SerializedName("phone_number")
    private String phoneNumber;

    private String province;

    private String district;
    private String ward;
    private String street;

    @SerializedName("is_default")
    private boolean isDefault;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProvince() {
        return province;
    }

    public String getDistrict() {
        return district;
    }

    public String getWard() {
        return ward;
    }

    public String getStreet() {
        return street;
    }


    public boolean isDefault() {
        return isDefault;
    }


    public Address(int id, int userId, String recipient, String phoneNumber, String province, String district, String ward, String street, boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.recipient = recipient;
        this.phoneNumber = phoneNumber;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.street = street;
        this.isDefault = isDefault;
    }


}
