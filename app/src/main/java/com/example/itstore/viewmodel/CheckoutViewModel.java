package com.example.itstore.viewmodel;

import static androidx.lifecycle.AndroidViewModel_androidKt.getApplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.CartItem;
import com.example.itstore.model.CreateOrderRequest;
import com.example.itstore.model.OrderCreateResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutViewModel extends AndroidViewModel {

    private final MutableLiveData<List<CartItem>> checkoutItems = new MutableLiveData<>();
    private final MutableLiveData<Double> subtotalPrice = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> shippingFee = new MutableLiveData<>(30000.0);
    private final MutableLiveData<Double> totalDiscount = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> finalTotalPrice = new MutableLiveData<>(0.0);
    private final MutableLiveData<Boolean> isOrderSuccess = new MutableLiveData<>();

    private final MutableLiveData<String> orderError = new MutableLiveData<>();

    public CheckoutViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<CartItem>> getCheckoutItems() { return checkoutItems; }
    public LiveData<Double> getSubtotalPrice() { return subtotalPrice; }
    public LiveData<Double> getShippingFee() { return shippingFee; }
    public LiveData<Double> getTotalDiscount() { return totalDiscount; }
    public LiveData<Double> getFinalTotalPrice() { return finalTotalPrice; }
    public LiveData<Boolean> getIsOrderSuccess() { return isOrderSuccess; }
    public LiveData<String> getOrderError() { return orderError; }



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

    public void placeOrder(CreateOrderRequest request) {
        RetrofitClient.getApiService(getApplication()).createOrder(request).enqueue(new Callback<OrderCreateResponse>() {
            @Override
            public void onResponse(Call<OrderCreateResponse> call, Response<OrderCreateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        isOrderSuccess.setValue(true);
                    } else {
                        orderError.setValue(response.body().getMessage());
                    }
                } else {
                    orderError.setValue("Lỗi khi tạo đơn hàng!");
                }
            }

            @Override
            public void onFailure(Call<OrderCreateResponse> call, Throwable t) {
                orderError.setValue("Lỗi kết nối mạng!");
            }
        });
    }
}