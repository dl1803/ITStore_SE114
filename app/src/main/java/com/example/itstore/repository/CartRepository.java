package com.example.itstore.repository;

import android.content.Context;
import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.CartResponse;
import com.example.itstore.model.CouponResponse;
import com.example.itstore.model.UpdateCartItemRequest;
import retrofit2.Callback;

public class CartRepository {
    private static CartRepository instance;
    private final RetrofitClient.ApiService apiService;

    private CartRepository(Context context) {
        this.apiService = RetrofitClient.getApiService(context);
    }

    public static synchronized CartRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CartRepository(context.getApplicationContext());
        }
        return instance;
    }
    public void getCart(Callback<CartResponse> callback) {
        apiService.getCart().enqueue(callback);
    }

    public void updateCartItem(int variantId, int quantity, Callback<CartResponse> callback) {
        apiService.updateCartItem(variantId, new UpdateCartItemRequest(quantity)).enqueue(callback);
    }
    public void removeCartItem(int variantId, Callback<CartResponse> callback) {
        apiService.removeCartItem(variantId).enqueue(callback);
    }
    public void getCoupons(Callback<CouponResponse> callback) {
        apiService.getCoupons().enqueue(callback);
    }
}