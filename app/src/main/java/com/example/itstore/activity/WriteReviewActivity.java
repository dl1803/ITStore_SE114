package com.example.itstore.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.itstore.R;
import com.example.itstore.databinding.ActivityReviewBinding;

public class WriteReviewActivity extends AppCompatActivity {

    private ActivityReviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(v -> finish());

        binding.rbStar.setOnRatingBarChangeListener(
                (ratingBar, rating, fromUser) -> {
                    if (rating <= 1.0f) {
                        binding.tvRatingLabel.setText("Rất tệ");
                        binding.tvRatingLabel.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    } else if (rating <= 2.0f) {
                        binding.tvRatingLabel.setText("Tệ");
                        binding.tvRatingLabel.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    } else if (rating <= 3.0f) {
                        binding.tvRatingLabel.setText("Bình thường");
                        binding.tvRatingLabel.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                    } else if (rating <= 4.0f) {
                        binding.tvRatingLabel.setText("Tốt");
                        binding.tvRatingLabel.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                    } else {
                        binding.tvRatingLabel.setText("Tuyệt vời");
                        binding.tvRatingLabel.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                    }
                });


        binding.btnSubmitReview.setOnClickListener(v -> {
            float starCount = binding.rbStar.getRating();
            String reviewContent = binding.edtReviewContent.getText().toString();

            if (starCount == 0) {
                Toast.makeText(this, "Vui lòng chọn số sao đánh giá!", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Cảm ơn bạn đã đánh giá " + starCount + " sao!", Toast.LENGTH_LONG).show();
            finish();

        });

    }
}