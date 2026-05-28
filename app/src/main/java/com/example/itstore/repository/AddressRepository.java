package com.example.itstore.repository;

import android.content.Context;

import com.example.itstore.api.GhnApiClient;
import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.AddressRequest;
import com.example.itstore.model.AddressResponse;
import com.example.itstore.model.GhnDistrict;
import com.example.itstore.model.GhnProvince;
import com.example.itstore.model.GhnResponse;
import com.example.itstore.model.GhnWard;
import com.example.itstore.model.SingleAddressResponse;

import java.util.List;

import retrofit2.Callback;

public class AddressRepository {
    private static AddressRepository instance;

    private final RetrofitClient.ApiService apiService;
    private final GhnApiClient.GhnApiService ghnApiService;

    private final String GHN_TOKEN = "5369cdb2-3fd4-11f1-b84f-e215adfdd13e";

    private AddressRepository(Context context) {
        this.apiService = RetrofitClient.getApiService(context);
        this.ghnApiService = GhnApiClient.getApiService();
    }

    public static synchronized AddressRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AddressRepository(context.getApplicationContext());
        }
        return instance;
    }

    public void getAddresses(Callback<AddressResponse> callback) {
        apiService.getAddresses().enqueue(callback);
    }

    public void deleteAddress(int addressId, Callback<SingleAddressResponse> callback) {
        apiService.deleteAddress(addressId).enqueue(callback);
    }

    public void addAddress(AddressRequest request, Callback<SingleAddressResponse> callback) {
        apiService.addAddress(request).enqueue(callback);
    }

    public void updateAddress(int id, AddressRequest request, Callback<SingleAddressResponse> callback) {
        apiService.updateAddress(id, request).enqueue(callback);
    }

    public void setDefaultAddress(int addressId, Callback<SingleAddressResponse> callback) {
        apiService.setDefaultAddress(addressId).enqueue(callback);
    }
    public void getProvinces(Callback<GhnResponse<List<GhnProvince>>> callback) {
        ghnApiService.getProvinces(GHN_TOKEN).enqueue(callback);
    }

    public void getDistricts(int provinceId, Callback<GhnResponse<List<GhnDistrict>>> callback) {
        ghnApiService.getDistricts(GHN_TOKEN, provinceId).enqueue(callback);
    }

    public void getWards(int districtId, Callback<GhnResponse<List<GhnWard>>> callback) {
        ghnApiService.getWards(GHN_TOKEN, districtId).enqueue(callback);
    }
}