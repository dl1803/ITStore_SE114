package com.example.itstore.repository;

import android.content.Context;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.SingleProductResponse;

import retrofit2.Callback;

public class ProductRepository {
    private static ProductRepository instance;
    private final RetrofitClient.ApiService apiService;

    private ProductRepository(Context context) {
        this.apiService = RetrofitClient.getApiService(context);
    }

    public static synchronized ProductRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ProductRepository(context.getApplicationContext());
        }
        return instance;
    }
    public void getProductBySlug(String slug, Callback<SingleProductResponse> callback) {
        apiService.getProductBySlug(slug).enqueue(callback);
    }
}
