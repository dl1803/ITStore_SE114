package com.example.itstore.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.Address;
import com.example.itstore.model.AddressResponse;
import com.example.itstore.model.SingleAddressResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressViewModel extends ViewModel {
    private final MutableLiveData<List<Address>> addressList = new MutableLiveData<>();

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final MutableLiveData<Boolean> deleteSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> deleteMessage = new MutableLiveData<>();

    public LiveData<List<Address>> getAddressList() {
        return addressList;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getDeleteSuccess() {
        return deleteSuccess;
    }

    public LiveData<String> getDeleteMessage() {
        return deleteMessage;
    }

    public void fetchAddresses(Context context) {
        RetrofitClient.getApiService(context).getAddresses().enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSucess()) {
                        addressList.setValue(response.body().getData());
                    } else {
                        errorMessage.setValue(response.body().getMessage());
                    }
                } else {
                    errorMessage.setValue("Lỗi khi tải danh sách địa chỉ");
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                errorMessage.setValue("Lỗi kết nối mạng!");
            }
        });
    }

    public void deleteAddress(Context context, int addressId){
        RetrofitClient.getApiService(context).deleteAddress(addressId).enqueue(new Callback<SingleAddressResponse>() {
            @Override
            public void onResponse(Call<SingleAddressResponse> call, Response<SingleAddressResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    deleteSuccess.setValue(true);
                    deleteMessage.setValue(response.body().getMessage());
                } else {
                    deleteSuccess.setValue(false);
                    deleteMessage.setValue("Lỗi! Không thể xóa địa chỉ");
                }
            }

            @Override
            public void onFailure(Call<SingleAddressResponse> call, Throwable t) {
                deleteSuccess.setValue(false);
                deleteMessage.setValue("Lỗi kết nối mạng!");
            }
        });
    }


}
