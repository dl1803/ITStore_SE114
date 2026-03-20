package com.example.itstore.viewmodel;
import androidx.lifecycle.ViewModel;
import android.util.Log;

import com.example.itstore.model.CartItem;
import com.example.itstore.model.Product;
import com.example.itstore.utils.CartManager;

public class ProductDetailViewModel extends ViewModel{
    public void addToCart(Product product, int variantId, String variantName, double finalPrice) {
        CartItem newItem = new CartItem(0, 1, variantId, 1, product, variantName, finalPrice);
        Log.d("CART_TEST", "Đã thêm vô giỏ: " + product.getName() + " | Bản: " + variantName + " | DB_Variant_ID: " + variantId);
        CartManager.getInstance().addToCart(newItem);
    }
}
