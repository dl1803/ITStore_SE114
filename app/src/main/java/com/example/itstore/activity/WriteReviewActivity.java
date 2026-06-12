package com.example.itstore.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.itstore.R;
import com.example.itstore.adapter.ReviewImageAdapter;
import com.example.itstore.databinding.ActivityReviewBinding;
import com.example.itstore.utils.FileUtils;
import com.example.itstore.viewmodel.ReviewViewModel;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class WriteReviewActivity extends AppCompatActivity {

    private ActivityReviewBinding binding;
    private ReviewViewModel reviewViewModel;

    private int orderItemId = -1;
    private int reviewId = -1;
    private boolean isEditMode = false;

    private final List<Uri> selectedImageUris = new ArrayList<>();
    private final List<String> oldImageUrls = new ArrayList<>();
    private final List<String> deletedImagesList = new ArrayList<>();
    private final List<Object> allImagesToShow = new ArrayList<>();
    private ReviewImageAdapter imageAdapter;

    private final ActivityResultLauncher<Intent> pickImagesLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            selectedImageUris.add(data.getClipData().getItemAt(i).getUri());
                        }
                    } else if (data.getData() != null) {
                        selectedImageUris.add(data.getData());
                    }
                    updateImageGridUI();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reviewId = getIntent().getIntExtra("REVIEW_ID", -1);
        if (reviewId != -1) {
            isEditMode = true;
        } else {
            orderItemId = getIntent().getIntExtra("ORDER_ITEM_ID", -1);
        }

        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        setupObservers();

        binding.imgBack.setOnClickListener(v -> finish());

        if (isEditMode) {
            binding.btnSubmitReview.setText("Cập nhật đánh giá");
            float oldRating = getIntent().getFloatExtra("OLD_RATING", 5.0f);
            String oldComment = getIntent().getStringExtra("OLD_COMMENT");

            ArrayList<String> imagesFromIntent = getIntent().getStringArrayListExtra("OLD_IMAGES");
            if (imagesFromIntent != null) {
                oldImageUrls.addAll(imagesFromIntent);
            }

            binding.rbStar.setRating(oldRating);
            binding.edtReviewContent.setText(oldComment);
            updateRatingLabel(oldRating);
        }

        updateImageGridUI();

        binding.btnSelectImages.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            pickImagesLauncher.launch(Intent.createChooser(intent, "Chọn hình ảnh đánh giá"));
        });

        binding.rbStar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> updateRatingLabel(rating));

        binding.btnSubmitReview.setOnClickListener(v -> {
            float starCount = binding.rbStar.getRating();
            String reviewContent = binding.edtReviewContent.getText().toString().trim();

            if (starCount == 0) {
                Toast.makeText(this, "Vui lòng chọn số sao!", Toast.LENGTH_SHORT).show();
                return;
            }


            binding.btnSubmitReview.setEnabled(false);
            int ratingValue = (int) starCount;

            List<MultipartBody.Part> imageParts = new ArrayList<>();
            for (Uri uri : selectedImageUris) {
                File file = FileUtils.getFileFromUri(this, uri);
                if (file != null) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("images", file.getName(), requestFile);
                    imageParts.add(body);
                }
            }

            if (isEditMode) {
                String deletedImagesJson = new Gson().toJson(deletedImagesList);
                reviewViewModel.updateReview(reviewId, ratingValue, reviewContent, deletedImagesJson, imageParts);
            } else {
                reviewViewModel.submitReview(orderItemId, ratingValue, reviewContent, imageParts);
            }
        });
        String incomingName = getIntent().getStringExtra("PRODUCT_NAME");
        String incomingImage = getIntent().getStringExtra("PRODUCT_IMAGE");
        String incomingVariant = getIntent().getStringExtra("PRODUCT_VARIANT");

        if (incomingName != null && !incomingName.isEmpty()) {
            binding.tvProductName.setText(incomingName);
        }
        if (incomingVariant != null && !incomingVariant.isEmpty()) {
            binding.tvProductVariant.setText("Phân loại: " + incomingVariant);
        } else {
            binding.tvProductVariant.setText("Phân loại: Tiêu chuẩn");
        }
        if (incomingImage != null && !incomingImage.isEmpty()) {
            com.bumptech.glide.Glide.with(this)
                    .load(incomingImage)
                    .placeholder(R.drawable.ram1)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(binding.imgProduct);
        }
    }

    private void updateImageGridUI() {
        allImagesToShow.clear();
        allImagesToShow.addAll(oldImageUrls);
        allImagesToShow.addAll(selectedImageUris);

        if (imageAdapter == null) {
            imageAdapter = new ReviewImageAdapter(allImagesToShow, true, (position, item) -> {
                if (item instanceof String) {
                    String url = (String) item;
                    oldImageUrls.remove(url);
                    deletedImagesList.add(url);
                } else {
                    selectedImageUris.remove(item);
                }
                updateImageGridUI();
            });
            binding.rvSelectedImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.rvSelectedImages.setAdapter(imageAdapter);
        } else {
            imageAdapter.notifyDataSetChanged();
        }
    }

    private void updateRatingLabel(float rating) {
        if (rating <= 1.0f) binding.tvRatingLabel.setText("Rất tệ");
        else if (rating <= 2.0f) binding.tvRatingLabel.setText("Tệ");
        else if (rating <= 3.0f) binding.tvRatingLabel.setText("Bình thường");
        else if (rating <= 4.0f) binding.tvRatingLabel.setText("Tốt");
        else binding.tvRatingLabel.setText("Tuyệt vời");
    }

    private void setupObservers() {
        reviewViewModel.getReviewSuccess().observe(this, isSuccess -> {
            if (isSuccess != null && isSuccess) {
                String msg = isEditMode ? "Cập nhật thành công!" : "Đánh giá thành công!";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });

        reviewViewModel.getErrorMessage().observe(this, errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
                binding.btnSubmitReview.setEnabled(true);
            }
        });
    }
}