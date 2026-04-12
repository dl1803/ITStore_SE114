package com.example.itstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itstore.R;
import com.example.itstore.model.Discount;

import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder> {

    private List<Discount> discountList;

    private OnDiscountClickListener listener;

    public interface OnDiscountClickListener {
        void onDiscountClick(Discount discount);
    }

    public DiscountAdapter (List<Discount> discountList, OnDiscountClickListener listener ){
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
        Discount item = discountList.get(position);

        holder.tvCode.setText(item.getCode());
        holder.tvTitle.setText(item.getTitle());
        holder.tvCondition.setText(item.getCondition());
        holder.tvDate.setText(item.getDate());

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
