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
import com.example.itstore.model.ProductReviewsResponse;
import com.example.itstore.model.Review;
import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final List<ProductReviewsResponse.ReviewDetail> reviewList;

    public ReviewAdapter(List<ProductReviewsResponse.ReviewDetail> reviewList) {
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
        ProductReviewsResponse.ReviewDetail review = reviewList.get(position);
        if (review == null) return;

        holder.tvUserName.setText(review.getUser() != null ? review.getUser().getFullName() : "Khách hàng");
        holder.rbStar.setRating(review.getRating());
        String commentText = review.getComment();
        if (commentText == null || commentText.trim().equalsIgnoreCase("null") || commentText.trim().isEmpty()) {
            commentText = "";
        }
        if (review.getVariant() != null) {
            if (!commentText.isEmpty()) {
                commentText += "\n\n";
            }
            commentText += review.getVariant().getVariantText();
        }
        holder.tvReviewContent.setText(commentText);
        holder.tvReviewDate.setText(formatIsoDate(review.getCreatedAt()));

        if (review.getImages() != null && !review.getImages().isEmpty()) {
            holder.rvReviewImages.setVisibility(View.VISIBLE);
            List<Object> objectImages = new ArrayList<>();
            for (com.example.itstore.model.ProductReviewsResponse.ImageInfo img : review.getImages()) {
                objectImages.add(img.getImageUrl());
            }

            ReviewImageAdapter imageAdapter = new ReviewImageAdapter(objectImages, false, null);
            holder.rvReviewImages.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            holder.rvReviewImages.setAdapter(imageAdapter);
        } else {
            holder.rvReviewImages.setVisibility(View.GONE);
        }
    }
    private String formatIsoDate(String isoDate) {
        try {
            if (isoDate == null || isoDate.isEmpty()) return "";
            if (isoDate.length() >= 10) {
                String ymd = isoDate.substring(0, 10);
                String[] parts = ymd.split("-");
                if (parts.length == 3) {
                    return parts[2] + "/" + parts[1] + "/" + parts[0];
                }
            }
            return isoDate;
        } catch (Exception e) {
            return isoDate;
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