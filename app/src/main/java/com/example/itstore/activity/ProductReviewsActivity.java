package com.example.itstore.activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.itstore.R;
import com.example.itstore.adapter.ReviewAdapter;
import com.example.itstore.databinding.ActivityReviewBinding;
import com.example.itstore.databinding.ActivityReviewHistoryBinding;
import com.example.itstore.model.Review;

import java.util.List;

public class ProductReviewsActivity extends AppCompatActivity {

    private ActivityReviewHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(v -> finish());

        // hàm getSerializableExtra nhận dữ liệu ptap đã được đơn giản bằng (Serializable) trong intent -> ép về lại dl ban đầu để dùng
        List<Review> receivedReviews = (List<Review>)getIntent().getSerializableExtra("REVIEW_LIST");

        if (receivedReviews != null && !receivedReviews.isEmpty()) {
            binding.layoutEmptyReview.setVisibility(View.GONE);
            binding.rvReviewHistory.setVisibility(View.VISIBLE);

            ReviewAdapter adapter = new ReviewAdapter(receivedReviews);
            binding.rvReviewHistory.setLayoutManager(new LinearLayoutManager(this));
            binding.rvReviewHistory.setAdapter(adapter);
        } else {
            binding.layoutEmptyReview.setVisibility(View.VISIBLE);
            binding.rvReviewHistory.setVisibility(View.GONE);
        }
    }
}