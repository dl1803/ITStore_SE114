package com.example.itstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itstore.R;
import com.example.itstore.model.ReturnRequestDetailResponse;

import java.util.List;

public class ReturnDetailImageAdapter extends RecyclerView.Adapter<ReturnDetailImageAdapter.ViewHolder> {

    private final List<ReturnRequestDetailResponse.DetailData.ReturnImage> list;

    public ReturnDetailImageAdapter(List<ReturnRequestDetailResponse.DetailData.ReturnImage> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_return_detail_image, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReturnRequestDetailResponse.DetailData.ReturnImage image = list.get(position);
        Glide.with(holder.itemView.getContext())
                .load(image.getImageUrl())
                .placeholder(R.drawable.ic_search)
                .error(R.drawable.ic_search)
                .into(holder.imgReturnEvidence);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgReturnEvidence;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgReturnEvidence = itemView.findViewById(R.id.imgReturnEvidence);
        }
    }
}