package com.example.itstore.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itstore.activity.ProductDetailActivity;
import com.example.itstore.databinding.ItemProductOrderDetailBinding;
import com.example.itstore.model.Product;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    private List<Product> productList;
    public OrderDetailAdapter(List<Product> productList) {
        this.productList = productList;
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
        Product product = productList.get(position);

        holder.binding.tvProductName.setText(product.getName());
        holder.binding.tvProductPrice.setText(String.format("%,.0fđ", product.getPrice()));
        if (product.getVariants() != null && !product.getVariants().isEmpty()) {

            holder.binding.tvProductType.setText("Phân loại: " + product.getVariants().get(0).getVersion());
        } else {
            holder.binding.tvProductType.setText("Phân loại: Mặc định");
        }

        String imageUrl = product.getImageUrl();
        Glide.with(holder.itemView.getContext()).load(imageUrl).into(holder.binding.imgProduct);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.getId());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemProductOrderDetailBinding binding;
        public ViewHolder(ItemProductOrderDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}