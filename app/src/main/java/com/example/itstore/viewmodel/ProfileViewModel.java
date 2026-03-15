package com.example.itstore.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.LogoutRequest;
import com.example.itstore.model.LogoutResponse;
import com.example.itstore.model.ProfileResponse;
import com.example.itstore.model.User;
import com.example.itstore.utils.SharedPrefsManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends AndroidViewModel {

    private final MutableLiveData<User> userProfile = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLogout = new MutableLiveData<>();

    public LiveData<User> getUserProfile() {
        return userProfile;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLogout() {
        return isLogout;
    }


    public ProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchProfile() {

        // Mock dữ liệu của Dev
        String token = SharedPrefsManager.getInstance(getApplication()).getAccessToken();
        if ("token_gia_cua_dev".equals(token)) {
            User devUser = new User(99, "A Dev So Cool", "dev@itstore.com", "0999999999", "Dev");
            userProfile.setValue(devUser);
            return;
        }


        RetrofitClient.getApiService(getApplication()).getProfile().enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        userProfile.setValue(response.body().getData());
                    } else {
                        errorMessage.setValue("Lỗi từ server!");
                    }
                } else {
                    errorMessage.setValue("Token hết hạn hoặc chưa đăng nhập!");
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                errorMessage.setValue("Lỗi kết nối mạng!");
            }
        });
    }

    public void logout() {
        String refreshToken = SharedPrefsManager.getInstance(getApplication()).getRefreshToken();

        if (refreshToken == null || refreshToken.isEmpty()) {
            isLogout.setValue(true);
            return;
        }

        LogoutRequest request = new LogoutRequest(refreshToken);
        RetrofitClient.getApiService(getApplication()).logout(request).enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String mes = response.body().getMessage();
                    errorMessage.setValue(mes);
                    isLogout.setValue(true);
                } else {
                    errorMessage.setValue("Phiên đăng nhập hết hạn, đang đăng xuất...");
                    isLogout.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                isLogout.setValue(true);
            }
        });
    }

}
