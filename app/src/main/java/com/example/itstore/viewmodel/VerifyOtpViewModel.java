package com.example.itstore.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.AuthMessageResponse;
import com.example.itstore.model.ResendOtpRequest;
import com.example.itstore.model.VerifyOtpRequest;
import com.example.itstore.repository.AuthRepository;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtpViewModel extends AndroidViewModel {
    private final AuthRepository repository;
    private final MutableLiveData<Boolean> isResending = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isResendSuccess = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isVerifying = new MutableLiveData<>(false);
    private final MutableLiveData<String> verifySuccess = new MutableLiveData<>();
    private final MutableLiveData<String> verifyError = new MutableLiveData<>();

    private final MutableLiveData<String> resendSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> resendError = new MutableLiveData<>();
    public LiveData<Boolean> getIsVerifying() { return isVerifying; }
    public LiveData<String> getVerifySuccess() { return verifySuccess; }
    public LiveData<String> getVerifyError() { return verifyError; }

    public LiveData<String> getResendSuccess() { return resendSuccess; }
    public LiveData<String> getResendError() { return resendError; }
    public LiveData<Boolean> getIsResending() { return isResending; }
    public LiveData<Boolean> getIsResendSuccess() { return isResendSuccess; }
    public VerifyOtpViewModel(@NonNull Application application) {
        super(application);
        repository = AuthRepository.getInstance(application);
    }

    public void verifyOtp(String email, String code) {
        isVerifying.setValue(true);
        VerifyOtpRequest request = new VerifyOtpRequest(email, code);

        repository.verifyEmailOtp(request, new Callback<AuthMessageResponse>() {
            @Override
            public void onResponse(Call<AuthMessageResponse> call, Response<AuthMessageResponse> response) {
                isVerifying.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    verifySuccess.setValue(response.body().getMessage());
                } else {
                    try {
                        String errorStr = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorStr);
                        verifyError.setValue(jsonObject.has("message") ? jsonObject.getString("message") : jsonObject.getString("error"));
                    } catch (Exception e) { verifyError.setValue("Mã xác thực không hợp lệ!"); }
                }
            }
            @Override
            public void onFailure(Call<AuthMessageResponse> call, Throwable t) {
                isVerifying.setValue(false);
                verifyError.setValue("Lỗi kết nối mạng!");
            }
        });
    }

    public void resendOtp(String email) {
        isResending.setValue(true);
        ResendOtpRequest request = new ResendOtpRequest(email);

        repository.resendVerifyEmailOtp(request, new Callback<AuthMessageResponse>() {
            @Override
            public void onResponse(Call<AuthMessageResponse> call, Response<AuthMessageResponse> response) {
                isResending.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    resendSuccess.setValue(response.body().getMessage());
                } else {
                    try {
                        String errorStr = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorStr);
                        resendError.setValue(jsonObject.has("message") ? jsonObject.getString("message") : jsonObject.getString("error"));
                    } catch (Exception e) { resendError.setValue("Không thể gửi lại mã lúc này!"); }
                }
            }
            @Override
            public void onFailure(Call<AuthMessageResponse> call, Throwable t) {
                isResending.setValue(false);
                resendError.setValue("Lỗi kết nối mạng!");
            }
        });
    }
}