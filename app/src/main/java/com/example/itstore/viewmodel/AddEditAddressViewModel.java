package com.example.itstore.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.AddressRequest;
import com.example.itstore.model.GhnDistrict;
import com.example.itstore.model.GhnProvince;
import com.example.itstore.model.GhnResponse;
import com.example.itstore.model.GhnWard;
import com.example.itstore.model.SingleAddressResponse;
import com.example.itstore.repository.AddressRepository;

import androidx.lifecycle.AndroidViewModel;

import java.text.Collator;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditAddressViewModel extends AndroidViewModel {
    private final AddressRepository repository;
    private final MutableLiveData<Boolean> addSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();

    private final MutableLiveData<Integer> newAddressId = new MutableLiveData<>();

    private final MutableLiveData<Boolean> setDefaultSuccess = new MutableLiveData<>();
    private final MutableLiveData<List<GhnProvince>> provinceList = new MutableLiveData<>();
    private final MutableLiveData<List<GhnDistrict>> districtList = new MutableLiveData<>();
    private final MutableLiveData<List<GhnWard>> wardList = new MutableLiveData<>();
    public AddEditAddressViewModel(@NonNull Application application) {
        super(application);
        repository = AddressRepository.getInstance(application);
    }

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
    public LiveData<List<GhnProvince>> getProvinceList() {
        return provinceList;
    }

    public LiveData<List<GhnDistrict>> getDistrictList() {
        return districtList;
    }

    public LiveData<List<GhnWard>> getWardList() {
        return wardList;
    }



    public void addAddress(Context context, AddressRequest request){
        repository.addAddress(request, new Callback<SingleAddressResponse>() {
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
        repository.updateAddress(id, request, new Callback<SingleAddressResponse>() {

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
        repository.setDefaultAddress(addressId, new Callback<SingleAddressResponse>() {
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
    public void fetchProvinces() {
        repository.getProvinces(new Callback<GhnResponse<List<GhnProvince>>>() {
            @Override
            public void onResponse(Call<GhnResponse<List<GhnProvince>>> call, Response<GhnResponse<List<GhnProvince>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GhnProvince> list = response.body().getData();
                    Collator collator = Collator.getInstance(new Locale("vi", "VN"));
                    Collections.sort(list, (p1, p2) -> collator.compare(p1.getProvinceName(), p2.getProvinceName()));
                    provinceList.setValue(list);
                }
            }
            @Override public void onFailure(Call<GhnResponse<List<GhnProvince>>> call, Throwable t) {}
        });
    }
    public void fetchDistricts(int provinceId) {
        repository.getDistricts(provinceId, new Callback<GhnResponse<List<GhnDistrict>>>() {
            @Override
            public void onResponse(Call<GhnResponse<List<GhnDistrict>>> call, Response<GhnResponse<List<GhnDistrict>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GhnDistrict> list = response.body().getData();
                    Collator collator = Collator.getInstance(new Locale("vi", "VN"));
                    Collections.sort(list, (d1, d2) -> collator.compare(d1.getDistrictName(), d2.getDistrictName()));
                    districtList.setValue(list);
                }
            }
            @Override public void onFailure(Call<GhnResponse<List<GhnDistrict>>> call, Throwable t) {}
        });
    }
    public void fetchWards(int districtId) {
        repository.getWards(districtId, new Callback<GhnResponse<List<GhnWard>>>() {
            @Override
            public void onResponse(Call<GhnResponse<List<GhnWard>>> call, Response<GhnResponse<List<GhnWard>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GhnWard> list = response.body().getData();
                    Collator collator = Collator.getInstance(new Locale("vi", "VN"));
                    Collections.sort(list, (w1, w2) -> collator.compare(w1.getWardName(), w2.getWardName()));
                    wardList.setValue(list);
                }
            }
            @Override public void onFailure(Call<GhnResponse<List<GhnWard>>> call, Throwable t) {}
        });
    }

}
