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

    private final MutableLiveData<Integer> newAddressId = new MutableLiveData<>();

    private final MutableLiveData<Boolean> setDefaultSuccess = new MutableLiveData<>();



    public LiveData<Boolean> getAddSuccess() {
        return addSuccess;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public LiveData<Integer> getNewAddressId() {
        return newAddressId;
    }

    public LiveData<Boolean> getSetDefaultSuccess() {
        return setDefaultSuccess;
    }




    public void addAddress(Context context, AddressRequest request){
        RetrofitClient.getApiService(context).addAddress(request).enqueue(new Callback<SingleAddressResponse>() {
            @Override
            public void onResponse(Call<SingleAddressResponse> call, Response<SingleAddressResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        newAddressId.setValue(response.body().getData().getId());
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

    public void updateAddress(Context context, int id, AddressRequest request){
        RetrofitClient.getApiService(context).updateAddress(id, request).enqueue(new Callback<SingleAddressResponse>() {

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
                    message.setValue("Lỗi! Không thể cập nhật địa chỉ");
                }
            }

            @Override
            public void onFailure(Call<SingleAddressResponse> call, Throwable t) {
                addSuccess.setValue(false);
                message.setValue("Lỗi kết nối mạng!");
            }
        });
    }

    public void setDefaultAddress(Context context, int addressId){
        RetrofitClient.getApiService(context).setDefaultAddress(addressId).enqueue(new Callback<SingleAddressResponse>() {
            @Override
            public void onResponse(Call<SingleAddressResponse> call, Response<SingleAddressResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    setDefaultSuccess.setValue(true);
                    message.setValue(response.body().getMessage());
                } else {
                    setDefaultSuccess.setValue(false);
                    message.setValue("Lỗi! Không thể đặt địa chỉ mặc định");
                }
            }

            @Override
            public void onFailure(Call<SingleAddressResponse> call, Throwable t) {
                setDefaultSuccess.setValue(false);
                message.setValue("Lỗi kết nối mạng!");
            }
        });
    }

}
