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

import java.util.List;

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
        Product product = item.getProduct();

        holder.binding.tvProductName.setText(product.getName());
        holder.binding.tvProductPrice.setText(String.format("%,.0fđ", item.getPrice()));
        holder.binding.tvProductCount.setText("Số lượng: " + item.getQuantity());

        if (product.getVariants() != null && !product.getVariants().isEmpty()) {
            holder.binding.tvProductType.setText("Phân loại: " + product.getVariants().get(0).getVersion());
        } else {
            holder.binding.tvProductType.setText("Phân loại: Mặc định");
        }


        String imageUrl = product.getImageUrl();
        try {
            int imageResId = Integer.parseInt(imageUrl);
            Glide.with(holder.itemView.getContext()).load(imageResId).into(holder.binding.imgProduct);
        } catch (NumberFormatException e) {
            Glide.with(holder.itemView.getContext()).load(imageUrl).into(holder.binding.imgProduct);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.getId());
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