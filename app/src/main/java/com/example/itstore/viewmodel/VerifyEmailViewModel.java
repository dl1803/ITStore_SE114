package com.example.itstore.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.VerifyEmailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyEmailViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> isResending = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isResendSuccess = new MutableLiveData<>();

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isVerifying = new MutableLiveData<>();

    public LiveData<Boolean> getIsResending() { return isResending; }
    public LiveData<Boolean> getIsResendSuccess() { return isResendSuccess; }

    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<String> getSuccessMessage() { return successMessage; }

    public LiveData<Boolean> getIsVerifying() { return isVerifying; }

    public VerifyEmailViewModel(@NonNull Application application) {
        super(application);
    }
    public void resendEmail() {
        isResending.setValue(true);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                isResending.setValue(false);
                isResendSuccess.setValue(true);
            }
        }, 2000);
    }

    public void verifyEmailToken(String token) {
        RetrofitClient.getApiService(getApplication()).verifyEmail(token).enqueue(new Callback<VerifyEmailResponse>() {
            @Override
            public void onResponse(Call<VerifyEmailResponse> call, Response<VerifyEmailResponse> response) {
                isVerifying.setValue(true);

                if (response.isSuccessful() && response.body() != null) {
                    successMessage.setValue(response.body().getMessage());
                } else {
                    try {
                        String errorStr = response.errorBody().string();
                        org.json.JSONObject jsonObject = new org.json.JSONObject(errorStr);
                        errorMessage.setValue(jsonObject.getString("error"));
                    } catch (Exception e){
                        errorMessage.setValue("Xác thực thất bại! Vui lòng thử lại.");
                    }
                }
            }

            @Override
            public void onFailure(Call<VerifyEmailResponse> call, Throwable t) {
                isVerifying.setValue(false);
                errorMessage.setValue("Lỗi kết nối mạng!");
            }
        });
    }

}