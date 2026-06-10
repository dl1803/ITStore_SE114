package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NotificationListResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private NotificationData data;

    @SerializedName("pagination")
    private Pagination pagination;

    public boolean isSuccess() { return success; }
    public NotificationData getData() { return data; }
    public Pagination getPagination() { return pagination; }
    public static class NotificationData {
        @SerializedName("notifications")
        private List<ServerNotification> notifications;

        public List<ServerNotification> getNotifications() { return notifications; }
    }
    public static class ServerNotification {
        @SerializedName("id") private int id;
        @SerializedName("title") private String title;
        @SerializedName("body") private String body;
        @SerializedName("type") private String type;
        @SerializedName("created_at") private String createdAt;
        @SerializedName("user_notifications") private List<UserNotificationState> userNotifications;

        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getBody() { return body; }
        public String getType() { return type; }
        public String getCreatedAt() { return createdAt; }
        public List<UserNotificationState> getUserNotifications() { return userNotifications; }
    }
    public static class UserNotificationState {
        @SerializedName("is_read") private boolean isRead;
        public boolean isRead() { return isRead; }
    }
    public static class Pagination {
        @SerializedName("total") private int total;
        @SerializedName("page") private int page;
        @SerializedName("limit") private int limit;
        @SerializedName("hasMore") private boolean hasMore;

        public int getTotal() { return total; }
        public int getPage() { return page; }
        public int getLimit() { return limit; }
        public boolean isHasMore() { return hasMore; }
    }
}