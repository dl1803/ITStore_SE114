package com.example.itstore.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.itstore.model.ReturnRequestResponse;
import com.example.itstore.repository.OrderRepository;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefundRequestViewModel extends AndroidViewModel {

    private final OrderRepository repository;

    private final MutableLiveData<Boolean> isSubmitSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public RefundRequestViewModel(@NonNull Application application) {
        super(application);
        repository = OrderRepository.getInstance(application);
    }

    public LiveData<Boolean> getIsSubmitSuccess() { return isSubmitSuccess; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void submitRefundRequest(String orderId, String reason,
                                    Map<Integer, Integer> selectedItems,
                                    String conditionValue,
                                    List<Uri> selectedImages) {

        StringBuilder itemsJson = new StringBuilder("[");
        boolean first = true;
        for (Map.Entry<Integer, Integer> entry : selectedItems.entrySet()) {
            if (!first) itemsJson.append(",");
            itemsJson.append("{\"order_item_id\":").append(entry.getKey())
                    .append(",\"quantity\":").append(entry.getValue())
                    .append(",\"condition\":\"").append(conditionValue).append("\"}");
            first = false;
        }
        itemsJson.append("]");

        RequestBody orderIdPart = RequestBody.create(orderId, MediaType.parse("text/plain"));
        RequestBody reasonPart = RequestBody.create(reason, MediaType.parse("text/plain"));
        RequestBody itemsPart = RequestBody.create(itemsJson.toString(), MediaType.parse("text/plain"));

        List<MultipartBody.Part> imageParts = new ArrayList<>();
        for (Uri uri : selectedImages) {
            try {
                InputStream is = getApplication().getContentResolver().openInputStream(uri);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] temp = new byte[4096];
                int len;
                while ((len = is.read(temp)) != -1) {
                    buffer.write(temp, 0, len);
                }
                is.close();
                byte[] bytes = buffer.toByteArray();
                RequestBody rb = RequestBody.create(bytes, MediaType.parse("image/jpeg"));
                MultipartBody.Part part = MultipartBody.Part.createFormData(
                        "images", "img_" + System.currentTimeMillis() + ".jpg", rb);
                imageParts.add(part);
            } catch (Exception e) {
                errorMessage.setValue("Lỗi đọc ảnh, vui lòng thử lại");
                return;
            }
        }

        isLoading.setValue(true);
        repository.createReturnRequest(orderIdPart, reasonPart, itemsPart, imageParts,
                new Callback<ReturnRequestResponse>() {
                    @Override
                    public void onResponse(Call<ReturnRequestResponse> call,
                                           Response<ReturnRequestResponse> response) {
                        isLoading.setValue(false);
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            isSubmitSuccess.setValue(true);
                        } else {
                            String msg = response.body() != null ? response.body().getMessage() : "Gửi yêu cầu thất bại";
                            errorMessage.setValue(msg);
                        }
                    }

                    @Override
                    public void onFailure(Call<ReturnRequestResponse> call, Throwable t) {
                        isLoading.setValue(false);
                        errorMessage.setValue("Lỗi mạng");
                    }
                });
    }
}