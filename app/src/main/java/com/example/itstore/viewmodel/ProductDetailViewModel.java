package com.example.itstore.viewmodel;
import androidx.lifecycle.ViewModel;
import android.util.Log;

import com.example.itstore.model.Product;

public class ProductDetailViewModel extends ViewModel{
    public void addToCart(Product product, String ramOption, double finalPrice) {
        Log.d("CART_TEST", "Đã thêm vô giỏ: " + product.getName() + " | Bản: " + ramOption + " | Giá: " + finalPrice);
    }
}
