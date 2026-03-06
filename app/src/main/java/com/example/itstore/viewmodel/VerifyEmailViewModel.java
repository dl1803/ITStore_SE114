package com.example.itstore.viewmodel;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VerifyEmailViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isResending = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isResendSuccess = new MutableLiveData<>();

    public LiveData<Boolean> getIsResending() { return isResending; }
    public LiveData<Boolean> getIsResendSuccess() { return isResendSuccess; }

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
}