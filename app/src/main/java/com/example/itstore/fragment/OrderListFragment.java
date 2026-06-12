package com.example.itstore.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itstore.R;
import com.example.itstore.activity.OrderDetailActivity;
import com.example.itstore.adapter.OrderAdapter;
import com.example.itstore.model.Order;
import com.example.itstore.viewmodel.OrderHistoryViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderListFragment extends Fragment {
    private String tabStatus;
    private OrderAdapter adapter;

    private OrderHistoryViewModel viewModel;

    public static OrderListFragment newInstance(String tabStatus) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putString("STATUS", tabStatus);
        fragment.setArguments(args);
        return fragment;
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    viewModel.fetchOrderHistory();
                }
            }
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tabStatus = getArguments().getString("STATUS");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_list, container, false);

        RecyclerView rvOrders = v.findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new OrderAdapter();
        rvOrders.setAdapter(adapter);

        adapter.setOrderList(new ArrayList<>());
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        viewModel = new ViewModelProvider(requireActivity()).get(OrderHistoryViewModel.class);

        // hàm getViewLifecycleOwner() dùng để gắn observe vào view , khi view bị xóa thì sẽ xóa observe theo, tránh memory leak khi dùng this sẽ bị tạo lại observe khi chuyển tab
        viewModel.getOrderList().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null) {
                List<Order> filteredOrders = new ArrayList<>();
                for (Order order : orders) {
                    String statusVN = order.getStatusVN();

                    if (tabStatus.equals("Tất cả") || tabStatus.equals(statusVN)){
                        filteredOrders.add(order);
                    }
                }

                if (tabStatus.equals("Đã mua")) {

                    Collections.sort(filteredOrders, (o1, o2) -> {
                        boolean isRev1 = (o1.getItems() != null && !o1.getItems().isEmpty()) && o1.getItems().get(0).isReviewed();
                        boolean isRev2 = (o2.getItems() != null && !o2.getItems().isEmpty()) && o2.getItems().get(0).isReviewed();

                        return Boolean.compare(isRev1, isRev2);
                    });
                }

                adapter.setOrderList(filteredOrders);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && tabStatus.equals("Tất cả")){
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        if (tabStatus.equals("Tất cả") && viewModel.getOrderList().getValue() == null) {
            viewModel.fetchOrderHistory();
        }

        adapter.setOnOrderClickListener(order -> {
            Intent intent = new Intent(requireContext(), OrderDetailActivity.class);
            intent.putExtra("ORDER_ID", Integer.parseInt(order.getOrderId()));
            launcher.launch(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // gọi lại để update trạng thái nút Đánh giá
        if (viewModel != null) {
            viewModel.fetchOrderHistory();
        }
    }
}
