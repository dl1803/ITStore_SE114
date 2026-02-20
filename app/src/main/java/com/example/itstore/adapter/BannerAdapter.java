package com.example.itstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itstore.R;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder>{
    private List<Integer> bannerList;

    public BannerAdapter(List<Integer> bannerList) {
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
        return new BannerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        int bannerId = bannerList.get(position);
        holder.imgBannerItem.setImageResource(bannerId);
    }

    @Override
    public int getItemCount() {
        if (bannerList != null) {
            return bannerList.size();
        }
        return 0;
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBannerItem;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBannerItem = itemView.findViewById(R.id.imgBannerItem);
        }
    }

}
