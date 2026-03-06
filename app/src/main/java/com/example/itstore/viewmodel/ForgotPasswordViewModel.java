package com.example.itstore.viewmodel;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ForgotPasswordViewModel extends ViewModel {
    private final MutableLiveData<String> emailError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();


    public LiveData<String> getEmailError() { return emailError; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Boolean> getIsSuccess() { return isSuccess; }

    public void sendResetLink(String email) {
        emailError.setValue(null);

        if (email.isEmpty()) {
            emailError.setValue("Vui lòng nhập email");
            return;
        } else if (!email.matches("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
            emailError.setValue("Email không hợp lệ!");
            return;
        }

        isLoading.setValue(true);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                isLoading.setValue(false);
                isSuccess.setValue(true);
            }
        }, 2000);
    }
}