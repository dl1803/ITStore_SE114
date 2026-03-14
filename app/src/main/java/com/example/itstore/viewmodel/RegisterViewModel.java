package com.example.itstore.viewmodel;


import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.RegisterRequest;
import com.example.itstore.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterViewModel extends AndroidViewModel {
    private MutableLiveData <String> fullNameError = new MutableLiveData<>();
    private MutableLiveData <String> emailError = new MutableLiveData<>();
    private MutableLiveData <String> phoneError = new MutableLiveData<>();
    private MutableLiveData <String> passwordError = new MutableLiveData<>();
    private MutableLiveData <String> confirmPasswordError = new MutableLiveData<>();
    private MutableLiveData <String> registerSuccessMessage = new MutableLiveData<>();

    private MutableLiveData <String> apiError =new MutableLiveData<>();
    private MutableLiveData <Boolean> isLoading = new MutableLiveData<>();

    public LiveData<String> getFullNameError() {
        return fullNameError;
    }

    public LiveData<String> getEmailError() {
        return emailError;
    }

    public LiveData<String> getPhoneError() {
        return phoneError;
    }

    public LiveData<String> getPasswordError() {
        return passwordError;
    }

    public LiveData<String> getConfirmPasswordError() {
        return confirmPasswordError;
    }

    public LiveData<Boolean> getIsLoading(){
        return isLoading;
    }

    public LiveData<String> getRegisterSuccessMessage(){
        return registerSuccessMessage;
    }

    public LiveData<String> getApiError (){
        return apiError;
    }


    public RegisterViewModel (Application application){
        super(application);
    }

    public void register (String fullName, String email, String phone, String passwd, String confirmPasswd){
        boolean isValid = true;

        fullNameError.setValue(null);
        emailError.setValue(null);
        phoneError.setValue(null);
        passwordError.setValue(null);
        confirmPasswordError.setValue(null);
        registerSuccessMessage.setValue(null);
        apiError.setValue(null);



        if (confirmPasswd.isEmpty()) {
            confirmPasswordError.setValue("Vui lòng nhập xác nhận mật khẩu");
            isValid = false;
        } else if (!passwd.equals(confirmPasswd)) {
            confirmPasswordError.setValue("Mật khẩu không khớp!");
            isValid = false;
        }

        if (passwd.isEmpty()) {
            passwordError.setValue("Vui lòng nhập mật khẩu");
            isValid = false;
        } else if (passwd.length() < 8) {
            passwordError.setValue("Mật khẩu phải có ít nhất 8 ký tự");
            isValid = false;
        } else if (!passwd.matches(".*[a-z].*")) {
            passwordError.setValue("Mật khẩu phải có ít nhất 1 ký tự thường");
            isValid = false;
        } else if (!passwd.matches(".*[A-Z].*")) {
            passwordError.setValue("Mật khẩu phải có ít nhất 1 ký tự in hoa");
            isValid = false;
        } else if (!passwd.matches(".*[0-9].*")) {
            passwordError.setValue("Mật khẩu phải có ít nhất 1 số");
            isValid = false;
        } else if (!passwd.matches(".*[^a-zA-Z0-9].*")) {
            passwordError.setValue("Mật khẩu phải có ít nhất 1 ký tự đặc biệt");
            isValid = false;
        }

        if (phone.isEmpty()) {
            phoneError.setValue("Vui lòng nhập số điện thoại");
            isValid =false;
        } else if (!phone.matches("^(03|05|07|08|09)\\d{8}$")) {
            phoneError.setValue("SĐT không hợp lệ!");
            isValid = false;
        }

        if (email.isEmpty()) {
            emailError.setValue("Vui lòng nhập email");
            isValid = false;
        } else if (!email.matches("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
            emailError.setValue("Email không hợp lệ! Vui lòng nhập lại");
            isValid =false;
        }

        if (fullName.isEmpty()){
            fullNameError.setValue("Vui lòng nhập họ và tên");
            isValid = false;
        }


        if (isValid) {
            isLoading.setValue(true);

            RegisterRequest.Address address = new RegisterRequest.Address(
                    fullName, phone, "Chưa cập nhật", "Chưa cập nhật", "Chưa cập nhật", "Chưa cập nhật", true
            );

            RegisterRequest request = new RegisterRequest(fullName, email, phone, passwd, address);

            RetrofitClient.getApiService(getApplication()).register(request).enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    isLoading.setValue(false);

                    if (response.isSuccessful() && response.body() != null) {
                        registerSuccessMessage.setValue(response.body().getMessage());
                    } else {
                        try {
                            String errorStr = response.errorBody().string();
                            org.json.JSONObject jsonObject = new org.json.JSONObject(errorStr);
                            apiError.setValue(jsonObject.getString("error"));
                        } catch (Exception e) {
                            apiError.setValue("Đăng ký thất bại! Vui lòng thử lại.");
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    isLoading.setValue(false);
                    apiError.setValue("Lỗi kết nối Server! Vui lòng thử lại.");
                }
            });
        }

    }

}
