package com.example.itstore.adapter;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class ImagePagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Integer> listImages;
    public ImagePagerAdapter(List<Integer> listImages) {
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
        ((ImageView) holder.itemView).setImageResource(listImages.get(position));
    }

    @Override
    public int getItemCount() {
        return listImages == null ? 0 : listImages.size();
    }
}
