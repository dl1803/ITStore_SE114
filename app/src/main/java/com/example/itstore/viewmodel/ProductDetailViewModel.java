package com.example.itstore.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import android.util.Log;

import com.example.itstore.model.CartItem;
import com.example.itstore.model.Product;
import com.example.itstore.utils.CartManager;
import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.CartResponse;
import com.example.itstore.model.AddCartItemRequest;

public class ProductDetailViewModel extends AndroidViewModel {

    public ProductDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void addToCart(Product product, int variantId, String variantName, double finalPrice) {
        RetrofitClient.getApiService(getApplication())
                .addCartItem(new AddCartItemRequest(variantId, 1))
                .enqueue(new retrofit2.Callback<CartResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<CartResponse> call, retrofit2.Response<CartResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            android.widget.Toast.makeText(getApplication(), "Đã thêm vào giỏ hàng!", android.widget.Toast.LENGTH_SHORT).show();
                        } else {
                            android.widget.Toast.makeText(getApplication(), "Không thể thêm vào giỏ hàng", android.widget.Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<CartResponse> call, Throwable t) {
                        android.widget.Toast.makeText(getApplication(), "Lỗi mạng", android.widget.Toast.LENGTH_SHORT).show();
                    }
                });
    }
}