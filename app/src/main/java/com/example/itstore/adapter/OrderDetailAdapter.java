package com.example.itstore.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itstore.R;
import com.example.itstore.activity.ProductDetailActivity;
import com.example.itstore.databinding.ItemProductOrderDetailBinding;
import com.example.itstore.model.OrderItem;
import com.example.itstore.model.Product;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    private List<OrderItem> orderItemList;
    public OrderDetailAdapter(List<OrderItem> orderItemList ) {
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductOrderDetailBinding binding = ItemProductOrderDetailBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem item = orderItemList.get(position);

        holder.binding.tvProductName.setText(item.getProductName());
        holder.binding.tvProductPrice.setText(String.format("%,.0fđ", item.getPrice()));
        holder.binding.tvProductCount.setText("Số lượng: " + item.getQuantity());

        holder.binding.tvProductType.setText("Phân loại: " + item.getProductType());

        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_search)
                .error(R.drawable.ic_launcher_background)
                .into(holder.binding.imgProduct);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ProductDetailActivity.class);
            Product dummyProduct = new Product(item.getProductId(), 0, item.getProductName(), "", null, null, 0);
            dummyProduct.setSlug(String.valueOf(item.getProductId()));
            intent.putExtra("PRODUCT_INFO", dummyProduct);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderItemList != null ? orderItemList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemProductOrderDetailBinding binding;

        public ViewHolder(ItemProductOrderDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}