package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ReturnRequestListResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private List<ReturnRequestItem> data;

    public boolean isSuccess() { return success; }
    public List<ReturnRequestItem> getData() { return data; }

    public static class ReturnRequestItem {
        @SerializedName("id")
        private int id;

        @SerializedName("reason")
        private String reason;

        @SerializedName("status")
        private String status;

        @SerializedName("admin_note")
        private String adminNote;

        @SerializedName("refund_amount")
        private double refundAmount;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("return_items")
        private List<ReturnItem> returnItems;

        public int getId() { return id; }
        public String getReason() { return reason; }
        public String getStatus() { return status; }
        public String getAdminNote() { return adminNote; }
        public double getRefundAmount() { return refundAmount; }
        public String getCreatedAt() { return createdAt; }
        public List<ReturnItem> getReturnItems() { return returnItems; }

        public static class ReturnItem {
            @SerializedName("id")
            private int id;

            @SerializedName("name")
            private String name;

            @SerializedName("slug")
            private String slug;

            @SerializedName("quantity")
            private int quantity;

            @SerializedName("variant")
            private VariantSummary variant;

            public int getId() { return id; }
            public String getName() { return name; }
            public String getSlug() { return slug; }
            public int getQuantity() { return quantity; }
            public VariantSummary getVariant() { return variant; }
        }

        public static class VariantSummary {
            @SerializedName("id")
            private int id;

            @SerializedName("version")
            private String version;

            @SerializedName("color")
            private String color;

            @SerializedName("color_hex")
            private String colorHex;

            @SerializedName("image_url")
            private String imageUrl;

            public int getId() { return id; }
            public String getVersion() { return version; }
            public String getColor() { return color; }
            public String getColorHex() { return colorHex; }
            public String getImageUrl() { return imageUrl; }
        }
    }
}