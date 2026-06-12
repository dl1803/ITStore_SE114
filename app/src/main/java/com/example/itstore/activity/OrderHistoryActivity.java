package com.example.itstore.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.itstore.R;
import com.example.itstore.databinding.ActivityOrderHistoryBinding;
import com.example.itstore.fragment.OrderListFragment;
import com.example.itstore.fragment.ReturnRequestListFragment;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrderHistoryActivity extends AppCompatActivity {
    private ActivityOrderHistoryBinding binding;
    private final String[] tabTitles = new String[]{"Tất cả", "Chờ xác nhận", "Đang giao", "Đã giao", "Đã mua", "Trả hàng", "Đã hủy"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener( v -> finish());

        OrderPagerAdapter pagerAdapter = new OrderPagerAdapter(this);
        binding.viewListOrders.setAdapter(pagerAdapter);

        // Buộc ViewPager2 tải và giữ nguyên cả 7 tab trong bộ nhớ.
        // preload trước các tab gần đó, giữ Fragment trong RAM, không destroy khi vuốt
        binding.viewListOrders.setOffscreenPageLimit(7);

        new TabLayoutMediator(binding.tabLayoutOrderStatus, binding.viewListOrders,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();

        String tabToOpen = getIntent().getStringExtra("TAB_TO_OPEN");
        if (tabToOpen != null) {
            if (tabToOpen.equals("PENDING")) {
                binding.tabLayoutOrderStatus.getTabAt(1).select();
            }
        }

    }

    private class OrderPagerAdapter extends FragmentStateAdapter {
        public OrderPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (tabTitles[position].equals("Trả hàng")) {
                return new ReturnRequestListFragment();
            }
            return OrderListFragment.newInstance(tabTitles[position]);
        }

        @Override
        public int getItemCount() {
            return tabTitles.length;
        }

    }
}