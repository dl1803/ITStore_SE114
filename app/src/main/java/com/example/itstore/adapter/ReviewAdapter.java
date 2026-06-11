package com.example.itstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.itstore.R;
import com.example.itstore.model.Review;
import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        if (review == null) return;

        holder.tvUserName.setText(review.getUserName() != null ? review.getUserName() : "Khách hàng");
        holder.rbStar.setRating(review.getRating());
        holder.tvReviewContent.setText(review.getComment());
        holder.tvReviewDate.setText(review.getReadableDate() != null ? review.getReadableDate() : "");

        if (review.getImageUrls() != null && !review.getImageUrls().isEmpty()) {
            holder.rvReviewImages.setVisibility(View.VISIBLE);
            List<Object> objectImages = new ArrayList<>(review.getImageUrls());
            ReviewImageAdapter imageAdapter = new ReviewImageAdapter(objectImages, false, null);

            holder.rvReviewImages.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            holder.rvReviewImages.setAdapter(imageAdapter);
        } else {
            holder.rvReviewImages.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return reviewList == null ? 0 : reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvReviewContent, tvReviewDate;
        RatingBar rbStar;
        RecyclerView rvReviewImages;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            rbStar = itemView.findViewById(R.id.rbStar);
            tvReviewContent = itemView.findViewById(R.id.tvReviewContent);
            tvReviewDate = itemView.findViewById(R.id.tvReviewDate);
            rvReviewImages = itemView.findViewById(R.id.rvReviewImages);
        }
    }
}