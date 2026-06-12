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
import com.example.itstore.model.ReturnRequestListResponse;

import java.text.DecimalFormat;
import java.util.List;

public class ReturnRequestAdapter extends RecyclerView.Adapter<ReturnRequestAdapter.ViewHolder> {

    private List<ReturnRequestListResponse.ReturnRequestItem> list;
    private OnReturnRequestClickListener listener;

    public interface OnReturnRequestClickListener {
        void onReturnRequestClick(ReturnRequestListResponse.ReturnRequestItem item);
    }

    public void setOnReturnRequestClickListener(OnReturnRequestClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<ReturnRequestListResponse.ReturnRequestItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_return_request, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReturnRequestListResponse.ReturnRequestItem item = list.get(position);

        holder.tvReturnRequestId.setText("Mã YCTH: #" + item.getId());
        holder.tvReturnStatus.setText(item.getStatusVN());
        holder.tvReturnStatus.setTextColor(item.getStatusColor());
        holder.tvReturnReason.setText("Lý do: " + item.getReason());
        holder.tvReturnDate.setText(item.getCreatedAtFormatted());

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.tvRefundAmount.setText("Hoàn tiền: " + formatter.format(item.getRefundAmount()) + "đ");

        List<ReturnRequestListResponse.ReturnRequestItem.ReturnItem> returnItems = item.getReturnItems();
        if (returnItems != null && !returnItems.isEmpty()) {
            ReturnRequestListResponse.ReturnRequestItem.ReturnItem firstItem = returnItems.get(0);
            holder.tvReturnProductName.setText(firstItem.getName());

            ReturnRequestListResponse.ReturnRequestItem.VariantSummary variant = firstItem.getVariant();
            if (variant != null) {
                String variantText = variant.getVersion();
                if (variant.getColor() != null && !variant.getColor().isEmpty()) {
                    variantText += ", " + variant.getColor();
                }
                holder.tvReturnProductVariant.setText(variantText);

                Glide.with(holder.itemView.getContext())
                        .load(variant.getImageUrl())
                        .placeholder(R.drawable.ic_search)
                        .error(R.drawable.ic_search)
                        .into(holder.imgReturnProduct);
            }

            if (returnItems.size() > 1) {
                holder.tvReturnExtraItems.setVisibility(View.VISIBLE);
                holder.tvReturnExtraItems.setText("Và " + (returnItems.size() - 1) + " sản phẩm khác");
            } else {
                holder.tvReturnExtraItems.setVisibility(View.GONE);
            }
        }

        holder.btnReturnRequestDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReturnRequestClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvReturnRequestId, tvReturnStatus, tvReturnProductName, tvReturnProductVariant,
                tvReturnExtraItems, tvReturnReason, tvReturnDate, tvRefundAmount, btnReturnRequestDetail;
        ImageView imgReturnProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReturnRequestId = itemView.findViewById(R.id.tvReturnRequestId);
            tvReturnStatus = itemView.findViewById(R.id.tvReturnStatus);
            tvReturnProductName = itemView.findViewById(R.id.tvReturnProductName);
            tvReturnProductVariant = itemView.findViewById(R.id.tvReturnProductVariant);
            tvReturnExtraItems = itemView.findViewById(R.id.tvReturnExtraItems);
            tvReturnReason = itemView.findViewById(R.id.tvReturnReason);
            tvReturnDate = itemView.findViewById(R.id.tvReturnDate);
            tvRefundAmount = itemView.findViewById(R.id.tvRefundAmount);
            btnReturnRequestDetail = itemView.findViewById(R.id.btnReturnRequestDetail);
            imgReturnProduct = itemView.findViewById(R.id.imgReturnProduct);
        }
    }
}