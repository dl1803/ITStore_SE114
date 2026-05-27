package com.example.itstore.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.CancelOrderRequest;
import com.example.itstore.model.Order;
import com.example.itstore.model.SingleOrderResponse;
import com.example.itstore.repository.OrderRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailViewModel extends AndroidViewModel {
    private final OrderRepository repository;
    private final MutableLiveData<Order> orderDetail = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isCancelSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> cancelError = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isConfirmSuccess = new MutableLiveData<>();
    public LiveData<Boolean> getIsConfirmSuccess() { return isConfirmSuccess; }

    public OrderDetailViewModel(@NonNull Application application) {
        super(application);
        repository = OrderRepository.getInstance(application);
    }

    public LiveData<Order> getOrderDetail() { return orderDetail; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Boolean> getIsCancelSuccess() { return isCancelSuccess; }
    public LiveData<String> getCancelError() { return cancelError; }
    public void fetchOrderDetail(int orderId) {
        isLoading.setValue(true);
        repository.getOrderById(orderId, new Callback<SingleOrderResponse>() {
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

    public void cancelOrder(int orderId, String reason) {
        isLoading.setValue(true);
        CancelOrderRequest request = new CancelOrderRequest(reason);
        repository.cancelOrder(orderId, request, new Callback<SingleOrderResponse>() {
            @Override
            public void onResponse(Call<SingleOrderResponse> call, Response<SingleOrderResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    isCancelSuccess.setValue(true);
                } else {
                    cancelError.setValue("Lỗi: Không thể hủy đơn hàng!");
                }
            }

            @Override
            public void onFailure(Call<SingleOrderResponse> call, Throwable t) {
                isLoading.setValue(false);
                cancelError.setValue("Lỗi kết nối mạng!");
            }
        });
    }

    public void confirmReceived(int orderId) {
        repository.confirmReceived(orderId, new Callback<SingleOrderResponse>() {
            @Override
            public void onResponse(Call<SingleOrderResponse> call, Response<SingleOrderResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {

                    Order updatedOrder = orderDetail.getValue();
                    if (updatedOrder != null) {
                        updatedOrder.setStatus("completed");
                        orderDetail.setValue(updatedOrder);
                    }

                    isConfirmSuccess.setValue(true);
                } else {
                    cancelError.setValue("Lỗi: Không thể xác nhận!");
                }
            }

            @Override
            public void onFailure(Call<SingleOrderResponse> call, Throwable t) {
                cancelError.setValue("Lỗi kết nối mạng!");
            }
        });
    }
}

