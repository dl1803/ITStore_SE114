package com.example.itstore.repository;

import android.content.Context;
import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.MarkAsReadRequest;
import com.example.itstore.model.NotificationListResponse;
import com.example.itstore.model.NotificationResponse;
import com.example.itstore.model.NotificationUnreadCountResponse;
import com.example.itstore.model.TokenRegistrationRequest;

import retrofit2.Callback;

public class NotificationRepository {
    private static NotificationRepository instance;
    private final RetrofitClient.ApiService apiService;

    private NotificationRepository(Context context) {
        this.apiService = RetrofitClient.getApiService(context.getApplicationContext());
    }

    public static synchronized NotificationRepository getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationRepository(context);
        }
        return instance;
    }
    public void getNotifications(int page, int limit, String type, Callback<NotificationListResponse> callback) {
        apiService.getNotifications(page, limit, type).enqueue(callback);
    }
    public void getUnreadNotificationCount(Callback<NotificationUnreadCountResponse> callback) {
        apiService.getUnreadNotificationCount().enqueue(callback);
    }
    public void registerFCMToken(TokenRegistrationRequest request, Callback<NotificationResponse> callback) {
        apiService.registerFCMToken(request).enqueue(callback);
    }
    public void markAsRead(MarkAsReadRequest request, Callback<NotificationResponse> callback) {
        apiService.markAsRead(request).enqueue(callback);
    }
    public void markAllAsRead(Callback<NotificationResponse> callback) {
        apiService.markAllAsRead().enqueue(callback);
    }
}