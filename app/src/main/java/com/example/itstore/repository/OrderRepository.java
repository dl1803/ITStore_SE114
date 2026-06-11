package com.example.itstore.repository;

import android.content.Context;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.CouponResponse;
import com.example.itstore.model.CreateOrderRequest;
import com.example.itstore.model.CreateOrderResponse;
import com.example.itstore.model.OrderHistoryResponse;
import com.example.itstore.model.PayOsPaymentResponse;
import com.example.itstore.model.ShipmentFeeRequest;
import com.example.itstore.model.ShipmentFeeResponse;
import com.example.itstore.model.ReturnRequestResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import java.util.List;
import retrofit2.Callback;

public class OrderRepository {
    private static OrderRepository instance;
    private final RetrofitClient.ApiService apiService;

    private OrderRepository(Context context) {
        this.apiService = RetrofitClient.getApiService(context);
    }

    public static synchronized OrderRepository getInstance(Context context) {
        if (instance == null) {
            instance = new OrderRepository(context.getApplicationContext());
        }
        return instance;
    }
    public void createOrder(CreateOrderRequest request, Callback<CreateOrderResponse> callback) {
        apiService.createOrder(request).enqueue(callback);
    }
    public void calculateShippingFee(ShipmentFeeRequest request, Callback<ShipmentFeeResponse> callback) {
        apiService.calculateShippingFee(request).enqueue(callback);
    }
    public void getCoupons(Callback<CouponResponse> callback) {
        apiService.getCoupons().enqueue(callback);
    }
    public void createPayOsPaymentLink(int orderId, Callback<PayOsPaymentResponse> callback) {
        apiService.createPayOsPaymentLink(orderId).enqueue(callback);
    }
    public void getOrderHistory(Callback<OrderHistoryResponse> callback) {
        apiService.getOrderHistory().enqueue(callback);
    }
    public void getOrderById(int orderId, Callback<com.example.itstore.model.SingleOrderResponse> callback) {
        apiService.getOrderById(orderId).enqueue(callback);
    }
    public void cancelOrder(int orderId, com.example.itstore.model.CancelOrderRequest request, Callback<com.example.itstore.model.SingleOrderResponse> callback) {
        apiService.cancelOrder(orderId, request).enqueue(callback);
    }
    public void confirmReceived(int orderId, Callback<com.example.itstore.model.SingleOrderResponse> callback) {
        apiService.confirmReceived(orderId).enqueue(callback);
    }
    public void createReturnRequest(RequestBody orderId, RequestBody reason, RequestBody items,
                                    List<MultipartBody.Part> images,
                                    Callback<ReturnRequestResponse> callback) {
        apiService.createReturnRequest(orderId, reason, items, images).enqueue(callback);
    }
}