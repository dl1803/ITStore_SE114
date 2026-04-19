package com.example.itstore.api;

import android.content.Context;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

import com.example.itstore.model.AddressRequest;
import com.example.itstore.model.AddressResponse;
import com.example.itstore.model.ChangePasswordRequest;
import com.example.itstore.model.ChangePasswordResponse;
import com.example.itstore.model.ForgotPasswordRequest;
import com.example.itstore.model.ForgotPasswordResponse;
import com.example.itstore.model.GoogleLoginRequest;
import com.example.itstore.model.LoginRequest;
import com.example.itstore.model.LoginResponse;
import com.example.itstore.model.LogoutRequest;
import com.example.itstore.model.LogoutResponse;
import com.example.itstore.model.ProfileResponse;
import com.example.itstore.model.RefreshTokenRequest;
import com.example.itstore.model.RefreshTokenResponse;
import com.example.itstore.model.RegisterRequest;
import com.example.itstore.model.RegisterResponse;
import com.example.itstore.model.ResetPasswordRequest;
import com.example.itstore.model.ResetPasswordResponse;
import com.example.itstore.model.SingleAddressResponse;
import com.example.itstore.model.UpdateProfileRequest;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.96.130:3000/";
    private static Retrofit retrofit = null;

    public static ApiService getApiService(Context context) {
        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .authenticator(new TokenAuthenticator(context))
                    .build();

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    public interface ApiService {
        @POST("api/auth/login")
        Call<LoginResponse> login(@Body LoginRequest request);

        @POST("api/auth/register")
        Call<RegisterResponse> register(@Body RegisterRequest request);

        @POST("api/auth/forgot-password")
        Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest request);

        @POST("api/auth/logout")
        Call<LogoutResponse> logout(@Body LogoutRequest request);

        @POST("api/auth/reset-password")
        Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest request);

        @POST("api/auth/refresh")
        Call<RefreshTokenResponse> refreshToken(@Body RefreshTokenRequest request);

        @POST("api/auth/google")
        Call<LoginResponse> googleLogin(@Body GoogleLoginRequest request);

        @GET("api/users/me")
        Call<ProfileResponse> getProfile();

        @PUT("api/users/me")
        Call<ProfileResponse> updateProfile(@Body UpdateProfileRequest request);

        @PUT("api/users/me/password")
        Call<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest request);

        @Multipart
        @PATCH("api/users/me/avatar")
        Call<Void> updateAvatar(@Part MultipartBody.Part avatar);

        @GET("api/users/me/addresses")
        Call<AddressResponse> getAddresses();

        @POST("api/users/me/addresses")
        Call<SingleAddressResponse> addAddress(@Body AddressRequest request);

        @PUT("api/users/me/addresses/{id}")
        Call<SingleAddressResponse> updateAddress(@Path("id") int id, @Body AddressRequest request);

        @PATCH("api/users/me/addresses/{id}/default")
        Call<SingleAddressResponse> setDefaultAddress(@Path("id") int id);

        @DELETE("api/users/me/addresses/{id}")
        Call<SingleAddressResponse> deleteAddress(@Path("id") int id);

    }
}