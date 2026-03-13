package com.example.itstore.viewmodel;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.ForgotPasswordRequest;
import com.example.itstore.model.ForgotPasswordResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordViewModel extends ViewModel {
    private final MutableLiveData<String> emailError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();
    private final MutableLiveData<String> apiError = new MutableLiveData<>();



    public LiveData<String> getEmailError() { return emailError; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getSuccessMessage() { return successMessage; }
    public LiveData<String> getApiError() { return apiError; }


    public void sendResetLink(String email) {
        emailError.setValue(null);
        successMessage.setValue(null);
        apiError.setValue(null);
        boolean isValid = true;


        if (email.isEmpty()) {
            emailError.setValue("Vui lòng nhập email");
            isValid = false;
        } else if (!email.matches("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
            emailError.setValue("Email không hợp lệ!");
            isValid = false;
        }

        if (isValid) {
            isLoading.setValue(true);
            ForgotPasswordRequest request = new ForgotPasswordRequest(email);
            RetrofitClient.getApiService().forgotPassword(request).enqueue(new Callback<ForgotPasswordResponse>() {
                @Override
                public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                    isLoading.setValue(false);

                    if (response.isSuccessful() && response.body() != null) {
                        successMessage.setValue(response.body().getMessage());
                    } else {
                        try {
                            String errorStr = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(errorStr);
                            apiError.setValue(jsonObject.getString("error"));
                        } catch (Exception e) {
                            apiError.setValue("Gửi yêu cầu thất bại! Vui lòng thử lại.");
                        }
                    }
                }

                @Override
                public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                    isLoading.setValue(false);
                    apiError.setValue("Lỗi kết nối Server! Vui lòng thử lại.");
                }
            });

        }
    }
}