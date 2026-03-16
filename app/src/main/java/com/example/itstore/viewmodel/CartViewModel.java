package com.example.itstore.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends ViewModel {
    private MutableLiveData<List<CartItem>> cartItemsLiveData = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Double> totalPriceLiveData = new MutableLiveData<>(0.0);
    public MutableLiveData<List<CartItem>> getCartItems() {
        return cartItemsLiveData;
    }
    public MutableLiveData<Double> getTotalPrice() {
        return totalPriceLiveData;
    }
    public void calculateTotal() {
        double total = 0;
        List<CartItem> currentList = cartItemsLiveData.getValue();
        if (currentList != null) {
            for (CartItem item : currentList) {
                total += item.getPrice() * item.getQuantity();
            }
        }
        totalPriceLiveData.setValue(total);
    }

}
