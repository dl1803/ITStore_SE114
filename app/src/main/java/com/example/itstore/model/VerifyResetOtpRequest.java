package com.example.itstore.model;

public class VerifyResetOtpRequest {
    private String email;
    private String code;
    public VerifyResetOtpRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
