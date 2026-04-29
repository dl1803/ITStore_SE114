package com.example.itstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itstore.R;
import com.example.itstore.model.Coupon;
import com.example.itstore.model.Discount;

import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder> {

    private List<Coupon> discountList;

    private OnDiscountClickListener listener;

    public interface OnDiscountClickListener {
        void onDiscountClick(Coupon discount);
    }

    public DiscountAdapter (List<Coupon> discountList, OnDiscountClickListener listener ){
        this.discountList = discountList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discount, parent, false);
        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountAdapter.DiscountViewHolder holder, int position) {
        Coupon item = discountList.get(position);

        holder.tvCode.setText(item.getCode());
        String titleText = item.getDiscountType().equals("percent")
                ? "Giảm " + item.getDiscountValue() + "%"
                : "Giảm " + String.format("%,.0fđ", item.getDiscountValue());
        holder.tvTitle.setText(titleText);
        holder.tvCondition.setText("Đơn tối thiểu " + String.format("%,.0fđ", item.getMinOrderValue()));
        String rawDate = item.getExpiresAt();
        if (rawDate != null && rawDate.length() >= 10) {
            String dateOnly = rawDate.substring(0, 10);
            String[] parts = dateOnly.split("-"); // hàm split của String dùng để tách chuỗi thành mảng các chuỗi con dựa trên ký tự phân tách
            if (parts.length == 3) {
                holder.tvDate.setText("HSD: " + parts[2] + "/" + parts[1] + "/" + parts[0]);
            } else {
                holder.tvDate.setText("HSD: " + rawDate);
            }
        } else {
            holder.tvDate.setText("HSD: Không giới hạn");
        }

        holder.btnUse.setOnClickListener(v -> {
            listener.onDiscountClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return discountList == null ? 0 : discountList.size();
    }

    public static class DiscountViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCondition, tvDate, tvCode;
        AppCompatButton btnUse;

        public DiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tvDiscountName);
            tvTitle = itemView.findViewById(R.id.tvDiscount);
            tvCondition = itemView.findViewById(R.id.tvDiscountCondition);
            tvDate = itemView.findViewById(R.id.tvDiscountDate);
            btnUse = itemView.findViewById(R.id.btnUseDiscount);
        }

    }
}
