package com.example.itstore.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.itstore.utils.SharedPrefsManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();


        String token = SharedPrefsManager.getInstance(context).getAccessToken();

        if (token == null || token.isEmpty()){
            return chain.proceed(originalRequest);
        }

        Request modifiedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();

        return chain.proceed(modifiedRequest);
    }
}
