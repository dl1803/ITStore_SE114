package com.example.itstore.viewmodel;
import androidx.lifecycle.ViewModel;
import android.util.Log;

import com.example.itstore.model.CartItem;
import com.example.itstore.model.Product;
import com.example.itstore.utils.CartManager;

public class ProductDetailViewModel extends ViewModel{
    public void addToCart(Product product, int variantId, String variantName, int quantity) {

        Log.d("HANDOVER", "Sản phẩm: " + product.getName() + " | Bản: " + variantName);
        Log.d("HANDOVER", "Variant ID gửi lên API: " + variantId + " | Số lượng: " + quantity);
    }
}
