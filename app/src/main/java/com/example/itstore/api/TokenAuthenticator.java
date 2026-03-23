package com.example.itstore.api;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.itstore.activity.LoginActivity;
import com.example.itstore.model.RefreshTokenRequest;
import com.example.itstore.model.RefreshTokenResponse;
import com.example.itstore.utils.SharedPrefsManager;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {
    private final Context context;

    public TokenAuthenticator(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NonNull Response response) throws IOException {
        String refreshToken = SharedPrefsManager.getInstance(context).getRefreshToken();

        if (refreshToken == null || refreshToken.isEmpty()) {
            return null;
        }

        RefreshTokenRequest requestBody = new RefreshTokenRequest(refreshToken);

        try {
            retrofit2.Call<RefreshTokenResponse> call = RetrofitClient.getApiService(context).refreshToken(requestBody);
            retrofit2.Response<RefreshTokenResponse> tokenResponse = call.execute();
            if (tokenResponse.isSuccessful() && tokenResponse.body() != null) {
                String newAccessToken = tokenResponse.body().getAccessToken();

                SharedPrefsManager.getInstance(context).saveTokens(newAccessToken, refreshToken);

                return response.request().newBuilder().header("Authorization", "Bearer " + newAccessToken).build();
            } else {
                return forceLogout();
            }
        } catch (Exception e) {
            return null;
        }
    }

    private Request forceLogout() {
        SharedPrefsManager.getInstance(context).clear();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        return null;
    }
}
