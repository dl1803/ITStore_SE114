package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class ProductReviewsResponse implements Serializable {
    private boolean success;
    private List<ReviewDetail> data;
    private ReviewSummary summary;

    public boolean isSuccess() { return success; }
    public List<ReviewDetail> getData() { return data; }
    public ReviewSummary getSummary() { return summary; }

    public static class ReviewDetail implements Serializable {
        private int id;
        private int rating;
        private String comment;
        private boolean is_edited;
        private String created_at;
        private UserInfo user;
        private VariantInfo variant;
        private List<ImageInfo> images;

        public int getId() { return id; }
        public int getRating() { return rating; }
        public String getComment() { return comment; }
        public boolean isEdited() { return is_edited; }
        public String getCreatedAt() { return created_at; }
        public UserInfo getUser() { return user; }
        public VariantInfo getVariant() { return variant; }
        public List<ImageInfo> getImages() { return images; }
    }

    public static class UserInfo implements Serializable {
        private int id;
        @SerializedName(value = "full_name", alternate = {"fullName", "name"})
        private String full_name;

        public String getFullName() { return full_name; }
    }

    public static class VariantInfo implements Serializable {
        private int id;
        private String version;
        private String color;

        public String getVariantText() {
            return "Phân loại: " + (color != null ? color : "") + " - " + (version != null ? version : "");
        }
    }

    public static class ImageInfo implements Serializable {
        private String image_url;
        public String getImageUrl() { return image_url; }
    }

    public static class ReviewSummary implements Serializable {
        private int total_reviews;
        private double avg_rating;

        public int getTotalReviews() { return total_reviews; }
        public double getAvgRating() { return avg_rating; }
    }
}