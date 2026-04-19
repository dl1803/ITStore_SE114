package com.example.itstore.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itstore.R;
import com.example.itstore.adapter.OrderAdapter;
import com.example.itstore.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends Fragment {
    private String tabStatus;

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

        OrderAdapter adapter = new OrderAdapter();
        rvOrders.setAdapter(adapter);

        List<Order> fakeList = new ArrayList<>();

        if (tabStatus.equals("Tất cả") || tabStatus.equals("Chờ xác nhận")) {
            fakeList.add(new Order("#DH001", "Chờ xác nhận", "RAM Desktop Kingston Fury", "16GB", 1, 2, 3486000, R.drawable.ram1));
        }
        if (tabStatus.equals("Tất cả") || tabStatus.equals("Đang giao")) {
            fakeList.add(new Order("#DH002", "Đang giao", "Laptop Gaming Asus", "Đen", 1, 0, 25000000, R.drawable.ram1));
        }
        if (tabStatus.equals("Tất cả") || tabStatus.equals("Đã hủy")) {
            fakeList.add(new Order("#DH003", "Đã hủy", "Chuột Logitech", "Không dây", 2, 0, 450000, R.drawable.ram1));
        }
        if (tabStatus.equals("Tất cả") || tabStatus.equals("Đã giao")) {
            fakeList.add(new Order("#DH004", "Đã giao", "Chuột Logitech", "Không dây", 2, 0, 450000));
        }

        adapter.setOrderList(fakeList);

        return v;
    }



}
