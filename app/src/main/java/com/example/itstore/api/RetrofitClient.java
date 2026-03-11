package com.example.itstore.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import com.example.itstore.model.LoginRequest;
import com.example.itstore.model.LoginResponse;
import com.example.itstore.model.RegisterRequest;
import com.example.itstore.model.RegisterResponse;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:3000/api/";
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    public interface ApiService {
        @POST("auth/login")
        Call<LoginResponse> login(@Body LoginRequest request);

        @POST("auth/register")
        Call<RegisterResponse> register(@Body RegisterRequest request);
    }
}