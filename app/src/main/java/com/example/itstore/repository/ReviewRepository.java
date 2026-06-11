package com.example.itstore.repository;

import android.content.Context;
import com.example.itstore.api.RetrofitClient;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ReviewRepository {
    private final Context context;

    public ReviewRepository(Context context) {
        this.context = context;
    }

    public void createReview(int orderItemId, int rating, String comment, List<MultipartBody.Part> images, Callback<ResponseBody> callback) {
        RequestBody orderItemBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(orderItemId));
        RequestBody ratingBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(rating));
        RequestBody commentBody = RequestBody.create(MediaType.parse("text/plain"), comment);

        Call<ResponseBody> call = RetrofitClient.getApiService(context).createReview(orderItemBody, ratingBody, commentBody, images);
        call.enqueue(callback);
    }

    public void updateReview(int reviewId, int rating, String comment, String deletedImagesJson, List<MultipartBody.Part> images, Callback<ResponseBody> callback) {
        RequestBody ratingBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(rating));
        RequestBody commentBody = RequestBody.create(MediaType.parse("text/plain"), comment);
        RequestBody deletedImagesBody = RequestBody.create(MediaType.parse("text/plain"), deletedImagesJson);

        Call<ResponseBody> call = RetrofitClient.getApiService(context).updateReview(reviewId, ratingBody, commentBody, deletedImagesBody, images);
        call.enqueue(callback);
    }
}