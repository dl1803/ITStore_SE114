package com.example.itstore.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.itstore.api.RetrofitClient;
import com.example.itstore.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewViewModel extends AndroidViewModel {
    private final ReviewRepository repository;
    private final MutableLiveData<Boolean> reviewSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public ReviewViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ReviewRepository(application);
    }

    public LiveData<Boolean> getReviewSuccess() { return reviewSuccess; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public void submitReview(int orderItemId, int rating, String comment, List<MultipartBody.Part> images) {
        repository.createReview(orderItemId, rating, comment, images, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) reviewSuccess.setValue(true);
                else errorMessage.setValue("Lỗi tạo đánh giá: " + response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errorMessage.setValue(t.getMessage());
            }
        });
    }
    public void updateReview(int reviewId, int rating, String comment, String deletedImagesJson, List<MultipartBody.Part> images) {
        repository.updateReview(reviewId, rating, comment, deletedImagesJson, images, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) reviewSuccess.setValue(true);
                else errorMessage.setValue("Lỗi cập nhật: " + response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errorMessage.setValue(t.getMessage());
            }
        });
    }
}