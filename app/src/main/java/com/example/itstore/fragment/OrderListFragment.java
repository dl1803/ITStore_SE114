package com.example.itstore.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itstore.R;
import com.example.itstore.adapter.OrderAdapter;
import com.example.itstore.model.Order;
import com.example.itstore.viewmodel.OrderHistoryViewModel;

import java.util.ArrayList;
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
                    String status = "Chờ xác nhận";
                    if (order.getStatus().equalsIgnoreCase("pending")) status = "Chờ xác nhận";
                    else if (order.getStatus().equalsIgnoreCase("processing")) status = "Đang giao";
                    else if (order.getStatus().equalsIgnoreCase("delivered")) status = "Đã giao";
                    else if (order.getStatus().equalsIgnoreCase("cancelled")) status = "Đã hủy";
                    if (tabStatus.equals("Tất cả") || tabStatus.equals(status)){
                        filteredOrders.add(order);
                    }
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
    }
}
