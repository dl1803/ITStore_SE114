package com.example.itstore.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.Order;
import com.example.itstore.model.SingleOrderResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailViewModel extends AndroidViewModel {

    private final MutableLiveData<Order> orderDetail = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public OrderDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Order> getOrderDetail() { return orderDetail; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void fetchOrderDetail(int orderId) {
        isLoading.setValue(true);
        RetrofitClient.getApiService(getApplication()).getOrderById(orderId).enqueue(new Callback<SingleOrderResponse>() {
            @Override
            public void onResponse(Call<SingleOrderResponse> call, Response<SingleOrderResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    orderDetail.setValue(response.body().getData());
                } else {
                    error.setValue("Lỗi: Không thể lấy thông tin đơn hàng");
                }
            }

            @Override
            public void onFailure(Call<SingleOrderResponse> call, Throwable t) {
                isLoading.setValue(false);
                error.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}

