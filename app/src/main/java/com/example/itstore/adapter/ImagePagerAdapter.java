package com.example.itstore.adapter;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itstore.R;

import java.util.List;
public class ImagePagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> listImages;
    public ImagePagerAdapter(List<String> listImages) {
        this.listImages = listImages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return new RecyclerView.ViewHolder(imageView) {};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageView imageView = (ImageView) holder.itemView;
        String imageUrl = listImages.get(position);
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_search)
                .error(R.drawable.ic_search)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return listImages == null ? 0 : listImages.size();
    }
}
