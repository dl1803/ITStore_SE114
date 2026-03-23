package com.example.itstore.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.GoogleLoginRequest;
import com.example.itstore.model.LoginRequest;
import com.example.itstore.model.LoginResponse;
import com.example.itstore.utils.SharedPrefsManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<String> emailError = new MutableLiveData<>();
    private MutableLiveData<String> passwordError = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<LoginResponse> loginSuccessData = new MutableLiveData<>();
    private MutableLiveData<String> apiError = new MutableLiveData<>();


    public LiveData<String> getEmailError () {return emailError;}
    public LiveData<String> getPasswordError () {return passwordError;}
    public LiveData<Boolean> getIsLoading () {return isLoading;}
    public LiveData<LoginResponse> getLoginSuccessData () {return loginSuccessData;}
    public LiveData<String> getApiError () {return apiError;}


    public LoginViewModel(Application application){
        super(application);
    }
    public void login(String email, String passwd) {
        boolean isValid = true;

        emailError.setValue(null);
        passwordError.setValue(null);
        loginSuccessData.setValue(null);
        apiError.setValue(null);



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

                LoginRequest request = new LoginRequest(email, passwd);

                RetrofitClient.getApiService(getApplication()).login(request).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        isLoading.setValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            loginSuccessData.setValue(response.body());
                        } else {
                            try {
                                String errorStr = response.errorBody().string();
                                org.json.JSONObject jsonObject = new org.json.JSONObject(errorStr);
                                String serverError = jsonObject.getString("message");
                                apiError.setValue(serverError);
                            } catch (Exception e) {
                                apiError.setValue("Email hoặc mật khẩu không đúng!");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        isLoading.setValue(false);
                        apiError.setValue("Lỗi kết nối Server! Vui lòng thử lại.");
                    }
                });
            }
    }

    public void googleLogin(String id_token){
        isLoading.setValue(true);

        GoogleLoginRequest request = new GoogleLoginRequest(id_token);

        RetrofitClient.getApiService(getApplication()).googleLogin(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    String accessToken = response.body().getAccessToken();
                    String refreshToken = response.body().getRefreshToken();

                    SharedPrefsManager.getInstance(getApplication()).saveTokens(accessToken, refreshToken);

                    loginSuccessData.setValue(response.body());
                } else {
                    apiError.setValue("Xác thực Google thất bại. Vui lòng thử lại!");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                isLoading.setValue(false);
                apiError.setValue("Lỗi kết nối Server! Vui lòng thử lại.");
            }
        });
    }
}
