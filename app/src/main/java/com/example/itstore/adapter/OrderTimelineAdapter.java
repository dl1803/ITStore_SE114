package com.example.itstore.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.itstore.databinding.ItemOrderTimelineBinding;
import com.example.itstore.model.OrderTimeline;

import java.util.List;

public class OrderTimelineAdapter extends RecyclerView.Adapter<OrderTimelineAdapter.ViewHolder> {

    private List<OrderTimeline> list;

    public OrderTimelineAdapter(List<OrderTimeline> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderTimelineBinding binding = ItemOrderTimelineBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderTimeline timeline = list.get(position);
        holder.binding.tvStatusOrderTimeline.setText(timeline.getStatus());
        holder.binding.tvTimeOrderTimeline.setText(timeline.getTime());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrderTimelineBinding binding;
        public ViewHolder(ItemOrderTimelineBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}