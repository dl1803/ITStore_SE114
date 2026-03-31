package com.example.itstore.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.AddressRequest;
import com.example.itstore.model.SingleAddressResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditAddressViewModel extends ViewModel {
    private final MutableLiveData<Boolean> addSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();

    public LiveData<Boolean> getAddSuccess() {
        return addSuccess;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public void addAddress(Context context, AddressRequest request){
        RetrofitClient.getApiService(context).addAddress(request).enqueue(new Callback<SingleAddressResponse>() {
            @Override
            public void onResponse(Call<SingleAddressResponse> call, Response<SingleAddressResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        addSuccess.setValue(true);
                        message.setValue(response.body().getMessage());
                    } else {
                        addSuccess.setValue(false);
                        message.setValue(response.body().getMessage());
                    }
                } else {
                    addSuccess.setValue(false);
                    message.setValue("Lỗi! Không thể thêm địa chỉ");
                }
            }

            @Override
            public void onFailure(Call<SingleAddressResponse> call, Throwable t) {
                addSuccess.setValue(false);
                message.setValue("Lỗi kết nối mạng!");
            }
        });
    }
}
