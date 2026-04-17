package com.example.itstore.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itstore.R;
import com.example.itstore.activity.OrderDetailActivity;
import com.example.itstore.activity.WriteReviewActivity;
import com.example.itstore.model.Order;

import java.text.DecimalFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;

    public void setOrderList(List<Order> orderList){
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        if (order == null) return;

        holder.tvOrderId.setText("Mã ĐH: " + order.getOrderId());
        holder.tvOrderStatus.setText(order.getStatus());
        holder.tvProductName.setText(order.getProductName());
        holder.tvProductType.setText(order.getProductType());
        holder.tvQuantity.setText("x" + order.getQuantity());

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.tvTotalPrice.setText("Thành tiền: " + formatter.format(order.getTotalPrice()) + "đ");

        holder.btnOrderDetail.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), OrderDetailActivity.class);
            intent.putExtra("ORDER_DATA", order);
            v.getContext().startActivity(intent);
        });

        holder.btnReviewOrder.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), WriteReviewActivity.class);
            v.getContext().startActivity(intent);
        });

        if (order.getStatus().equalsIgnoreCase("Đã hủy")) {
            holder.tvOrderStatus.setTextColor(Color.parseColor("#FF0000"));
        } else if (order.getStatus().equalsIgnoreCase("Đã giao")) {
            holder.tvOrderStatus.setTextColor(Color.parseColor("#00FF00"));
        } else {
            holder.tvOrderStatus.setTextColor(Color.parseColor("#F57C00"));
        }

        if (order.getStatus().equalsIgnoreCase("Hoàn thành") || order.getStatus().equalsIgnoreCase("Đã giao")) {
            holder.btnReviewOrder.setVisibility(View.VISIBLE);
        } else {
            holder.btnReviewOrder.setVisibility(View.GONE);
        }

        if (order.getExtraItemsCount() > 0) {
            holder.tvTotalItems.setVisibility(View.VISIBLE);
            holder.tvTotalItems.setText("Và " + order.getExtraItemsCount() + " sản phẩm khác");
        } else {
            holder.tvTotalItems.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orderList == null ? 0 : orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderStatus, tvProductName, tvProductType;
        TextView tvQuantity, tvTotalItems, tvTotalPrice, btnOrderDetail, btnReviewOrder;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvProductName = itemView.findViewById(R.id.tvProductNameOrder);
            tvProductType = itemView.findViewById(R.id.tvProductTypeOrder);
            tvQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvTotalItems = itemView.findViewById(R.id.tvTotalItems);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPriceOrder);
            btnOrderDetail = itemView.findViewById(R.id.btnOrderDetail);
            btnReviewOrder = itemView.findViewById(R.id.btnReviewOrder);
        }
    }
}
