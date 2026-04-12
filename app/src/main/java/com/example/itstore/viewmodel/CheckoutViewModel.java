package com.example.itstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.itstore.model.CartItem;
import java.util.List;

public class CheckoutViewModel extends ViewModel {

    private final MutableLiveData<List<CartItem>> checkoutItems = new MutableLiveData<>();
    private final MutableLiveData<Double> subtotalPrice = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> shippingFee = new MutableLiveData<>(30000.0);
    private final MutableLiveData<Double> totalDiscount = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> finalTotalPrice = new MutableLiveData<>(0.0);

    public LiveData<List<CartItem>> getCheckoutItems() { return checkoutItems; }
    public LiveData<Double> getSubtotalPrice() { return subtotalPrice; }
    public LiveData<Double> getShippingFee() { return shippingFee; }
    public LiveData<Double> getTotalDiscount() { return totalDiscount; }
    public LiveData<Double> getFinalTotalPrice() { return finalTotalPrice; }

    public void loadCheckoutData(List<CartItem> items) {
        checkoutItems.setValue(items);
        calculateMoney(items);
    }

    private void calculateMoney(List<CartItem> items) {
        double subtotal = 0;
        double discount = 0;

        if (items != null) {
            for (CartItem item : items) {
                subtotal += (item.getPrice() * item.getQuantity());
            }
        }

        double ship = shippingFee.getValue() != null ? shippingFee.getValue() : 0;
        double finalPrice = subtotal + ship - discount;

        subtotalPrice.setValue(subtotal);
        totalDiscount.setValue(discount);
        finalTotalPrice.setValue(finalPrice);
    }

    public void applyDiscount(double discountAmount) {
        totalDiscount.setValue(discountAmount);

        double subtotal = subtotalPrice.getValue() != null ? subtotalPrice.getValue() : 0;
        double ship = shippingFee.getValue() != null ? shippingFee.getValue() : 0;

        double finalPrice = (subtotal + ship) - discountAmount;

        if(finalPrice < 0) finalPrice = 0;

        finalTotalPrice.setValue(finalPrice);
    }
}