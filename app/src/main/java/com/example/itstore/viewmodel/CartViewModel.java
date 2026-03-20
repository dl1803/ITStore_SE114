package com.example.itstore.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.model.CartItem;
import com.example.itstore.utils.CartManager;

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
                if (item.isSelected()) {
                    total += item.getPrice() * item.getQuantity();
                }
            }
        }
        totalPriceLiveData.setValue(total);
    }
    public void increaseQuantity(CartItem item, int position) {
        List<CartItem> currentList = cartItemsLiveData.getValue();
        if (currentList != null) {
            item.setQuantity(item.getQuantity() + 1);
            cartItemsLiveData.setValue(currentList);
            calculateTotal();
        }
    }
    public void decreaseQuantity(CartItem item, int position) {
        List<CartItem> currentList = cartItemsLiveData.getValue();
        if (currentList != null) {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            } else {
                currentList.remove(position);
            }
            cartItemsLiveData.setValue(currentList);
        }
    }
    public void removeItem(int position) {
        List<CartItem> currentList = cartItemsLiveData.getValue();
        if (currentList != null && position < currentList.size()) {
            currentList.remove(position);
            cartItemsLiveData.setValue(currentList);
            calculateTotal();
        }
    }
    public void toggleSelectAll(boolean isSelectAll) {
        List<CartItem> currentList = cartItemsLiveData.getValue();
        if (currentList != null) {
            for (CartItem item : currentList) {
                item.setSelected(isSelectAll);
            }
            cartItemsLiveData.setValue(currentList);
            calculateTotal();
        }
    }
    public void loadCartFromManager() {
        List<CartItem> realCart = CartManager.getInstance().getCartList();
        if (realCart != null) {
            cartItemsLiveData.setValue(realCart);
            calculateTotal();
        }
    }
    public void loadCartFromApi(List<CartItem> itemsFromApi) {
        cartItemsLiveData.setValue(itemsFromApi);
        calculateTotal();
    }

}
