package com.example.itstore.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.Address;
import com.example.itstore.model.AddressResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressViewModel extends ViewModel {
    private final MutableLiveData<List<Address>> addressList = new MutableLiveData<>();

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<List<Address>> getAddressList() {
        return addressList;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
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


}
