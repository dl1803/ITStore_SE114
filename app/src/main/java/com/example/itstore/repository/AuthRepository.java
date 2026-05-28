package com.example.itstore.repository;

import android.content.Context;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.AuthMessageResponse;
import com.example.itstore.model.GoogleLoginRequest;
import com.example.itstore.model.LoginRequest;
import com.example.itstore.model.LoginResponse;
import com.example.itstore.model.RegisterRequest;
import com.example.itstore.model.RegisterResponse;
import com.example.itstore.model.ResendOtpRequest;

import retrofit2.Callback;

public class AuthRepository {
    private static AuthRepository instance;
    private final RetrofitClient.ApiService apiService;

    private AuthRepository(Context context) {
        this.apiService = RetrofitClient.getApiService(context);
    }

    public static synchronized AuthRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AuthRepository(context.getApplicationContext());
        }
        return instance;
    }
    public void login(LoginRequest request, Callback<LoginResponse> callback) {
        apiService.login(request).enqueue(callback);
    }
    public void googleLogin(GoogleLoginRequest request, Callback<LoginResponse> callback) {
        apiService.googleLogin(request).enqueue(callback);
    }
    public void register(RegisterRequest request, Callback<RegisterResponse> callback) {
        apiService.register(request).enqueue(callback);
    }
    public void resendVerifyEmailOtp(ResendOtpRequest request, Callback<AuthMessageResponse> callback) {
        apiService.resendVerifyEmailOtp(request).enqueue(callback);
    }
    public void forgotPassword(com.example.itstore.model.ForgotPasswordRequest request, Callback<com.example.itstore.model.ForgotPasswordResponse> callback) {
        apiService.forgotPassword(request).enqueue(callback);
    }
    public void resetPassword(String token, com.example.itstore.model.ResetPasswordRequest request, Callback<com.example.itstore.model.AuthMessageResponse> callback) {
        apiService.resetPassword(token, request).enqueue(callback);
    }
    public void verifyEmailOtp(com.example.itstore.model.VerifyOtpRequest request, Callback<com.example.itstore.model.AuthMessageResponse> callback) {
        apiService.verifyEmailOtp(request).enqueue(callback);
    }
    public void verifyResetPasswordOtp(com.example.itstore.model.VerifyResetOtpRequest request, Callback<com.example.itstore.model.VerifyResetOtpResponse> callback) {
        apiService.verifyResetPasswordOtp(request).enqueue(callback);
    }
}