package com.example.itstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itstore.R;
import com.example.itstore.model.ReturnRequestDetailResponse;

import java.text.DecimalFormat;
import java.util.List;

public class ReturnDetailItemAdapter extends RecyclerView.Adapter<ReturnDetailItemAdapter.ViewHolder> {

    private final List<ReturnRequestDetailResponse.DetailData.ReturnItem> list;

    public ReturnDetailItemAdapter(List<ReturnRequestDetailResponse.DetailData.ReturnItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_return_detail_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReturnRequestDetailResponse.DetailData.ReturnItem item = list.get(position);

        holder.tvItemProductName.setText(item.getName());

        ReturnRequestDetailResponse.DetailData.VariantSummary variant = item.getVariant();
        if (variant != null) {
            String variantText = variant.getVersion();
            if (variant.getColor() != null && !variant.getColor().isEmpty()) {
                variantText += ", " + variant.getColor();
            }
            holder.tvItemVariant.setText(variantText);

            Glide.with(holder.itemView.getContext())
                    .load(variant.getImageUrl())
                    .placeholder(R.drawable.ic_search)
                    .error(R.drawable.ic_search)
                    .into(holder.imgItemProduct);
        }

        holder.tvItemCondition.setText("Tình trạng: " + item.getConditionVN());

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.tvItemQuantityPrice.setText("x" + item.getQuantity() + " - " + formatter.format(item.getUnitPrice()) + "đ");
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItemProduct;
        TextView tvItemProductName, tvItemVariant, tvItemCondition, tvItemQuantityPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemProduct = itemView.findViewById(R.id.imgItemProduct);
            tvItemProductName = itemView.findViewById(R.id.tvItemProductName);
            tvItemVariant = itemView.findViewById(R.id.tvItemVariant);
            tvItemCondition = itemView.findViewById(R.id.tvItemCondition);
            tvItemQuantityPrice = itemView.findViewById(R.id.tvItemQuantityPrice);
        }
    }
}