package com.example.itstore.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.itstore.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyEmailViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> isResending = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isResendSuccess = new MutableLiveData<>();


    public LiveData<Boolean> getIsResending() { return isResending; }
    public LiveData<Boolean> getIsResendSuccess() { return isResendSuccess; }


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
}