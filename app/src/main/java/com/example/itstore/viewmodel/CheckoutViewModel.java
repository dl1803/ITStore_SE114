package com.example.itstore.viewmodel;

import static androidx.lifecycle.AndroidViewModel_androidKt.getApplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.api.GhnApiClient;
import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.CartItem;
import com.example.itstore.model.Coupon;
import com.example.itstore.model.CouponResponse;
import com.example.itstore.model.CreateOrderRequest;
import com.example.itstore.model.GhnFeeData;
import com.example.itstore.model.GhnFeeRequest;
import com.example.itstore.model.GhnResponse;
import com.example.itstore.model.OrderCreateResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutViewModel extends AndroidViewModel {

    private final String GHN_TOKEN = "5369cdb2-3fd4-11f1-b84f-e215adfdd13e";
    private final int GHN_SHOP_ID = 6403105;

    private final MutableLiveData<List<CartItem>> checkoutItems = new MutableLiveData<>();
    private final MutableLiveData<Double> subtotalPrice = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> shippingFee = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> totalDiscount = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> finalTotalPrice = new MutableLiveData<>(0.0);
    private final MutableLiveData<Boolean> isOrderSuccess = new MutableLiveData<>();

    private final MutableLiveData<String> orderError = new MutableLiveData<>();

    private final MutableLiveData<List<Coupon>> couponList = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedCouponId = new MutableLiveData<>(null);
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

    public LiveData<List<Coupon>> getCouponList() { return couponList; }
    public LiveData<Integer> getSelectedCouponId() { return selectedCouponId; }



    public void loadCheckoutData(List<CartItem> items) {
        checkoutItems.setValue(items);
        calculateMoney(items);
    }

    private void calculateMoney(List<CartItem> items) {
        double subtotal = 0;
        double discount = totalDiscount.getValue() != null ? totalDiscount.getValue() : 0;

        if (items != null) {
            for (CartItem item : items) {
                subtotal += (item.getPrice() * item.getQuantity());
            }
        }

        double ship = shippingFee.getValue() != null ? shippingFee.getValue() : 0;
        double finalPrice = subtotal + ship - discount;
        if(finalPrice < 0) finalPrice = 0;

        subtotalPrice.setValue(subtotal);
        totalDiscount.setValue(discount);
        finalTotalPrice.setValue(finalPrice);
    }

    public void applyDiscount(double discountAmount, int couponId) {
        totalDiscount.setValue(discountAmount);
        selectedCouponId.setValue(couponId);
        calculateMoney(checkoutItems.getValue());
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

    public void calculateShippingFee(int toDistrictId, String toWardCode) {
        int totalWeight = 0;
        List<CartItem> items = checkoutItems.getValue();
        if (items != null) {
            for (CartItem item : items) {
                totalWeight += (item.getQuantity() * 500);  // Giả sử 500g/sản phẩm
            }
        }

        if (totalWeight == 0)
        {return;}

        int fromDistrictId = 3695; // ID của kho Thủ đức (dựa theo địa chỉ của của hàng)

        GhnFeeRequest request = new GhnFeeRequest(fromDistrictId, toDistrictId, toWardCode, totalWeight);

        GhnApiClient.getApiService().getShippingFee(GHN_TOKEN, GHN_SHOP_ID, request)
                .enqueue(new retrofit2.Callback<GhnResponse<GhnFeeData>>() {
                    @Override
                    public void onResponse(Call<GhnResponse<GhnFeeData>> call, Response<GhnResponse<GhnFeeData>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                            double realFee = response.body().getData().getTotalFee();
                            shippingFee.setValue(realFee);

                            calculateMoney(checkoutItems.getValue());
                        } else {
                            shippingFee.setValue(30000.0);
                            calculateMoney(checkoutItems.getValue());
                    }
                        }

                    @Override
                    public void onFailure(Call<GhnResponse<GhnFeeData>> call, Throwable t) {
                        shippingFee.setValue(30000.0);
                        calculateMoney(checkoutItems.getValue());
                    }
                });
    }
    public void fetchActiveCoupons() {
        RetrofitClient.getApiService(getApplication()).getCoupons().enqueue(new Callback<CouponResponse>() {

            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    couponList.setValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {

            }
        });
    }

}