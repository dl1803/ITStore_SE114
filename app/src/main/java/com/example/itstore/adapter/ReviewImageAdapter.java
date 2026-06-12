package com.example.itstore.adapter;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.itstore.R;
import android.widget.ImageView;
import java.util.List;

public class ReviewImageAdapter extends RecyclerView.Adapter<ReviewImageAdapter.ViewHolder> {

    private final List<Object> imageList;
    private final boolean showDeleteBtn;
    private OnImageDeleteListener deleteListener;

    public interface OnImageDeleteListener {
        void onDelete(int position, Object item);
    }

    public ReviewImageAdapter(List<Object> imageList, boolean showDeleteBtn, OnImageDeleteListener deleteListener) {
        this.imageList = imageList;
        this.showDeleteBtn = showDeleteBtn;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object item = imageList.get(position);
        Object loadTarget = item;
        if (item instanceof String) {
            String url = (String) item;
            if (!url.startsWith("http")) {
                loadTarget = "http://10.0.2.2:3000/" + url;
            }
        }

        final Object finalTarget = loadTarget;
        Glide.with(holder.itemView.getContext())
                .load(item)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.imgReview);

        if (!showDeleteBtn) {
            holder.imgReview.setOnClickListener(v -> {
                Dialog dialog = new Dialog(holder.itemView.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);

                ImageView imageView = new ImageView(holder.itemView.getContext());
                imageView.setBackgroundColor(android.graphics.Color.BLACK);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                Glide.with(holder.itemView.getContext())
                        .load(finalTarget)
                        .into(imageView);

                dialog.setContentView(imageView);
                imageView.setOnClickListener(v1 -> dialog.dismiss());

                dialog.show();
            });
        }
        if (showDeleteBtn) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDelete(position, item);
                }
            });
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return imageList != null ? imageList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgReview, btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgReview = itemView.findViewById(R.id.imgReview);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}