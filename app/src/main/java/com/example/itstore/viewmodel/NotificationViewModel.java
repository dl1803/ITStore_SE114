package com.example.itstore.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.itstore.model.ItemNotification;
import com.example.itstore.model.MarkAsReadRequest;
import com.example.itstore.model.NotificationListResponse;
import com.example.itstore.model.NotificationResponse;
import com.example.itstore.model.NotificationUnreadCountResponse;
import com.example.itstore.model.TokenRegistrationRequest;
import com.example.itstore.repository.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationViewModel extends AndroidViewModel {
    private final NotificationRepository repository;

    private final MutableLiveData<List<ItemNotification>> notificationsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> unreadCountLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        this.repository = NotificationRepository.getInstance(application);
    }

    public LiveData<List<ItemNotification>> getNotificationsLiveData() { return notificationsLiveData; }
    public LiveData<Integer> getUnreadCountLiveData() { return unreadCountLiveData; }
    public LiveData<String> getErrorLiveData() { return errorLiveData; }
    public void fetchNotifications(int page, int limit, String type) {
        repository.getNotifications(page, limit, type, new Callback<NotificationListResponse>() {
            @Override
            public void onResponse(Call<NotificationListResponse> call, Response<NotificationListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    NotificationListResponse.NotificationData responseData = response.body().getData();
                    List<ItemNotification> mappedList = new ArrayList<>();

                    if (responseData != null && responseData.getNotifications() != null) {
                        for (NotificationListResponse.ServerNotification sn : responseData.getNotifications()) {
                            boolean isRead = false;
                            if (sn.getUserNotifications() != null && !sn.getUserNotifications().isEmpty()) {
                                isRead = sn.getUserNotifications().get(0).isRead();
                            }

                            mappedList.add(new ItemNotification(
                                    String.valueOf(sn.getId()), sn.getTitle(), sn.getBody(), sn.getCreatedAt(), isRead
                            ));
                        }
                    }
                    notificationsLiveData.setValue(mappedList);
                } else {
                    errorLiveData.setValue("Không thể tải thông báo!");
                }
            }

            @Override
            public void onFailure(Call<NotificationListResponse> call, Throwable t) {
                errorLiveData.setValue("Lỗi kết nối mạng!");
            }
        });
    }

    public void fetchUnreadCount() {
        repository.getUnreadNotificationCount(new Callback<NotificationUnreadCountResponse>() {
            @Override
            public void onResponse(Call<NotificationUnreadCountResponse> call, Response<NotificationUnreadCountResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    unreadCountLiveData.setValue(response.body().getCount());
                }
            }
            @Override
            public void onFailure(Call<NotificationUnreadCountResponse> call, Throwable t) {}
        });
    }

    public void addRealtimeNotification(ItemNotification newNoti) {
        List<ItemNotification> currentList = notificationsLiveData.getValue();
        if (currentList == null) currentList = new ArrayList<>();

        currentList.add(0, newNoti);
        notificationsLiveData.setValue(currentList);
        if (!newNoti.isRead()) {
            Integer currentCount = unreadCountLiveData.getValue();
            if (currentCount == null) currentCount = 0;
            unreadCountLiveData.setValue(currentCount + 1);
        }
    }
    public void registerFCMToken(String token) {
        TokenRegistrationRequest request =
                new TokenRegistrationRequest(token);

        repository.registerFCMToken(request, new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Log.d("FCM_LOG", "Đăng ký Token qua ViewModel thành công!");
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Log.e("FCM_LOG", "Lỗi ViewModel khi đăng ký token: " + t.getMessage());
            }
        });
    }
    public void markNotificationAsRead(int notificationId) {
        List<Integer> idList = new ArrayList<>();
        idList.add(notificationId);

        MarkAsReadRequest request = new MarkAsReadRequest(idList);

        repository.markAsRead(request, new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<ItemNotification> currentList = notificationsLiveData.getValue();
                    if (currentList != null) {
                        for (ItemNotification item : currentList) {
                            if (item.getId().equals(String.valueOf(notificationId))) {
                                item.setRead(true);
                                break;
                            }
                        }
                        notificationsLiveData.setValue(currentList);
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                errorLiveData.setValue("Lỗi mạng, không thể cập nhật trạng thái!");
            }
        });
    }
    public void markAllNotificationsAsRead() {
        repository.markAllAsRead(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<ItemNotification> currentList = notificationsLiveData.getValue();
                    if (currentList != null) {
                        for (ItemNotification item : currentList) {
                            item.setRead(true);
                        }
                        notificationsLiveData.setValue(currentList);
                    }
                    unreadCountLiveData.setValue(0);
                } else {
                    errorLiveData.setValue("Không thể đánh dấu đọc tất cả!");
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                errorLiveData.setValue("Lỗi kết nối mạng!");
            }
        });
    }
}