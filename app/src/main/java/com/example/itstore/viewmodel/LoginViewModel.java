package com.example.itstore.viewmodel;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class LoginViewModel extends ViewModel {
    private MutableLiveData<String> emailError = new MutableLiveData<>();
    private MutableLiveData<String> passwordError = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoginSuccess = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();


    public LiveData<String> getEmailError () {return emailError;}
    public LiveData<String> getPasswordError () {return passwordError;}
    public LiveData<Boolean> getIsLoginSuccess () {return isLoginSuccess;}
    public LiveData<Boolean> getIsLoading () {return isLoading;}

    public void login(String email, String passwd) {
        boolean isValid = true;

        emailError.setValue(null);
        passwordError.setValue(null);

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

        if (email.isEmpty()) {
            emailError.setValue("Vui lòng nhập email");
            isValid = false;
        } else if (!email.matches("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
            emailError.setValue("Email không hợp lệ!");
            isValid = false;
        }



        if (isValid) {
            isLoading.setValue(true);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    isLoading.setValue(false);
                    isLoginSuccess.setValue(true);
                }
            }, 2000);
        }

    }
}
