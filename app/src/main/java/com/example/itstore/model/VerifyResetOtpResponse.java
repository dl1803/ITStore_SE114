package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class VerifyResetOtpResponse {
    @SerializedName("reset_token")
    private String resetToken;
    private String message;
    public String getResetToken() { return resetToken; }
    public String getMessage() { return message; }
}
