package com.example.itstore.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.ForgotPasswordRequest;
import com.example.itstore.model.ForgotPasswordResponse;
import com.example.itstore.model.VerifyResetOtpRequest;
import com.example.itstore.model.VerifyResetOtpResponse;
import com.example.itstore.repository.AuthRepository;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyResetOtpViewModel extends AndroidViewModel {
    private final AuthRepository repository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> apiError = new MutableLiveData<>();
    private final MutableLiveData<String> tokenSuccessData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isResending = new MutableLiveData<>(false);
    private final MutableLiveData<String> resendSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> resendError = new MutableLiveData<>();
    public VerifyResetOtpViewModel(@NonNull Application application) {
        super(application);
        repository = AuthRepository.getInstance(application);
    }

    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getApiError() { return apiError; }
    public LiveData<String> getTokenSuccessData() { return tokenSuccessData; }
    public LiveData<Boolean> getIsResending() { return isResending; }
    public LiveData<String> getResendSuccess() { return resendSuccess; }
    public LiveData<String> getResendError() { return resendError; }


    public void verifyCode(String email, String code) {
        isLoading.setValue(true);
        VerifyResetOtpRequest request = new VerifyResetOtpRequest(email, code);

        repository.verifyResetPasswordOtp(request, new Callback<VerifyResetOtpResponse>() {
            @Override
            public void onResponse(Call<VerifyResetOtpResponse> call, Response<VerifyResetOtpResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    tokenSuccessData.setValue(response.body().getResetToken());
                } else {
                    try {
                        String errorStr = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorStr);
                        apiError.setValue(jsonObject.getString("message"));
                    } catch (Exception e) { apiError.setValue("Mã xác thực không hợp lệ hoặc đã hết hạn!"); }
                }
            }
            @Override
            public void onFailure(Call<VerifyResetOtpResponse> call, Throwable t) {
                isLoading.setValue(false);
                apiError.setValue("Lỗi kết nối mạng!");
            }
        });
    }

    public void resendCode(String email) {
        isResending.setValue(true);
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);

        repository.forgotPassword(request, new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                isResending.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    resendSuccess.setValue("Đã gửi lại mã. Vui lòng kiểm tra email!");
                } else {
                    resendError.setValue("Lỗi! Không thể gửi lại mã.");
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                isResending.setValue(false);
                resendError.setValue("Lỗi mạng! Không thể gửi lại.");
            }
        });
    }
}
