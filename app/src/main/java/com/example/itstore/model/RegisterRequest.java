package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("full_name")
    private String fullName;

    private String email;

    @SerializedName("phone_number")
    private String phoneNumber;

    private String password;

    private Address address;

    public RegisterRequest(String fullName, String email, String phoneNumber, String password, Address address) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
    }
    public static class Address {
        private String recipient;

        @SerializedName("phone_number")
        private String phoneNumber;

        private String province;
        private String district;
        private String ward;
        private String street;

        @SerializedName("is_default")
        private boolean isDefault;

        public Address(String recipient, String phoneNumber, String province, String district, String ward, String street, boolean isDefault) {
            this.recipient = recipient;
            this.phoneNumber = phoneNumber;
            this.province = province;
            this.district = district;
            this.ward = ward;
            this.street = street;
            this.isDefault = isDefault;
        }
    }
}