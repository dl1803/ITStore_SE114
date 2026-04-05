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
import com.example.itstore.adapter.NotificationAdapter;
import com.example.itstore.databinding.ActivityNotificationBinding;
import com.example.itstore.model.ItemNotification;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private ActivityNotificationBinding binding;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(v -> finish());

        adapter = new NotificationAdapter();
        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNotifications.setAdapter(adapter);

        List<ItemNotification> fakeList = new ArrayList<>();

        fakeList.add(new ItemNotification("1", "Đơn hàng đã giao thành công",
                "Đơn hàng #DH123456 của bạn đã được giao thành công. Hãy cho chúng tôi biết cảm nhận của bạn về sản phẩm nhé!",
                "10:30 - 4/04/2026", false));

        fakeList.add(new ItemNotification("2", "Đơn hàng đang được giao",
                "Đơn hàng #DH123456 đang trên đường giao đến bạn. Shipper sẽ gọi trước khi giao, bạn chú ý điện thoại nhé.",
                "08:15 - 3/04/2026", true));

        fakeList.add(new ItemNotification("3", "🔥 Cảnh báo Sale khủng 🔥",
                "Flash Sale giữa đêm giảm đến 50% các dòng Laptop Gaming. Chốt đơn ngay!",
                "00:00 - 4/04/2026", false));

        adapter.setNotiList(fakeList);

        if (fakeList.isEmpty()) {
            binding.rvNotifications.setVisibility(View.GONE);
            binding.emptyNotiState.getRoot().setVisibility(View.VISIBLE);
        } else {
            binding.rvNotifications.setVisibility(View.VISIBLE);
            binding.emptyNotiState.getRoot().setVisibility(View.GONE);
        }
    }
}