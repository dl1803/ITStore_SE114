package com.example.itstore.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.ResetPasswordRequest;
import com.example.itstore.model.ResetPasswordResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordViewModel extends AndroidViewModel {
    private MutableLiveData<String> passwordError = new MutableLiveData<>();
    private MutableLiveData<String> confirmPasswordError = new MutableLiveData<>();
    private MutableLiveData<String> apiError = new MutableLiveData<>();
    private MutableLiveData<String> successMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();


    public LiveData<String> getPasswordError() {
        return passwordError;
    }
    public LiveData<String> getConfirmPasswordError() {
        return confirmPasswordError;
    }
    public LiveData<String> getApiError(){
        return apiError;
    }
    public LiveData<String> getSuccessMessage(){
        return successMessage;
    }
    public LiveData<Boolean> getIsLoading(){
        return isLoading;
    }

    public ResetPasswordViewModel(@NonNull Application application) {
        super(application);
    }

    public void resetPassword(String token, String newPass, String confirmPass){
        boolean isValid = true;

        passwordError.setValue(null);
        confirmPasswordError.setValue(null);
        apiError.setValue(null);

        if (newPass.isEmpty()) {
            passwordError.setValue("Vui lòng nhập mật khẩu mới");
            isValid = false;
        } else if (newPass.length() < 8) {
            passwordError.setValue("Mật khẩu phải có ít nhất 8 ký tự");
            isValid = false;
        } else if (!newPass.matches(".*[a-z].*")) {
            passwordError.setValue("Mật khẩu phải có ít nhất 1 ký tự thường");
            isValid = false;
        } else if (!newPass.matches(".*[A-Z].*")) {
            passwordError.setValue("Mật khẩu phải có ít nhất 1 ký tự in hoa");
            isValid = false;
        } else if (!newPass.matches(".*[0-9].*")) {
            passwordError.setValue("Mật khẩu phải có ít nhất 1 số");
            isValid = false;
        } else if (!newPass.matches(".*[^a-zA-Z0-9].*")) {
            passwordError.setValue("Mật khẩu phải có ít nhất 1 ký tự đặc biệt");
            isValid = false;
        }

        if (confirmPass.isEmpty()) {
            confirmPasswordError.setValue("Vui lòng xác nhận mật khẩu");
            isValid = false;
        } else if (!confirmPass.equals(newPass)) {
            confirmPasswordError.setValue("Mật khẩu xác nhận không khớp");
            isValid = false;
        }

        if (token == null || token.isEmpty()) {
            apiError.setValue("Lỗi Token. Vui lòng mở lại link từ Email!");
            isValid = false;
        }

        if (isValid) {
            isLoading.setValue(true);
            ResetPasswordRequest request = new ResetPasswordRequest(token, newPass);

            RetrofitClient.getApiService(getApplication()).resetPassword(request).enqueue(new Callback<ResetPasswordResponse>() {
                @Override
                public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                    isLoading.setValue(false);

                    if (response.isSuccessful() && response.body() != null) {
                        successMessage.setValue(response.body().getMessage());
                    } else {
                        try {
                            String errorStr = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(errorStr);
                            apiError.setValue(jsonObject.getString("error"));
                        } catch (Exception e) {
                            apiError.setValue("Lỗi xác thực. Link có thể đã hết hạn!");
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                    isLoading.setValue(false);
                    apiError.setValue("Lỗi kết nối Server! Vui lòng thử lại.");
                }
            });
        }
    }

}
