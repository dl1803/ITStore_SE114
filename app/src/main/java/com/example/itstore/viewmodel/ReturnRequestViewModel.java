package com.example.itstore.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.itstore.model.ReturnRequestDetailResponse;
import com.example.itstore.model.ReturnRequestListResponse;
import com.example.itstore.repository.OrderRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReturnRequestViewModel extends AndroidViewModel {
    private final OrderRepository repository;

    private final MutableLiveData<List<ReturnRequestListResponse.ReturnRequestItem>> returnRequestList = new MutableLiveData<>();
    private final MutableLiveData<ReturnRequestDetailResponse.DetailData> returnRequestDetail = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public ReturnRequestViewModel(@NonNull Application application) {
        super(application);
        repository = OrderRepository.getInstance(application);
    }

    public LiveData<List<ReturnRequestListResponse.ReturnRequestItem>> getReturnRequestList() { return returnRequestList; }
    public LiveData<ReturnRequestDetailResponse.DetailData> getReturnRequestDetail() { return returnRequestDetail; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void fetchReturnRequests() {
        isLoading.setValue(true);
        repository.getMyReturnRequests(new Callback<ReturnRequestListResponse>() {
            @Override
            public void onResponse(Call<ReturnRequestListResponse> call, Response<ReturnRequestListResponse> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        returnRequestList.setValue(response.body().getData());
                    } else {
                        errorMessage.setValue("Lỗi từ server: Không lấy được danh sách yêu cầu trả hàng");
                    }
                } else if (response.code() == 401) {
                    errorMessage.setValue("Không xác định được người dùng");
                } else if (response.code() == 403) {
                    errorMessage.setValue("Bạn không có quyền truy cập");
                } else {
                    errorMessage.setValue("Lỗi kết nối hoặc phiên đăng nhập hết hạn!");
                }
            }

            @Override
            public void onFailure(Call<ReturnRequestListResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    public void fetchReturnRequestDetail(int id) {
        isLoading.setValue(true);
        repository.getMyReturnRequestDetail(id, new Callback<ReturnRequestDetailResponse>() {
            @Override
            public void onResponse(Call<ReturnRequestDetailResponse> call, Response<ReturnRequestDetailResponse> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        returnRequestDetail.setValue(response.body().getData());
                    } else {
                        errorMessage.setValue("Lỗi từ server: Không lấy được chi tiết yêu cầu trả hàng");
                    }
                } else if (response.code() == 400) {
                    errorMessage.setValue("Yêu cầu trả hàng không tồn tại");
                } else if (response.code() == 401) {
                    errorMessage.setValue("Không xác định được người dùng");
                } else if (response.code() == 403) {
                    errorMessage.setValue("Bạn không có quyền truy cập");
                } else {
                    errorMessage.setValue("Lỗi kết nối hoặc phiên đăng nhập hết hạn!");
                }
            }

            @Override
            public void onFailure(Call<ReturnRequestDetailResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }
}