package com.example.itstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itstore.R;
import com.example.itstore.model.Specification;

import java.util.List;

public class SpecAdapter extends RecyclerView.Adapter<SpecAdapter.SpecViewHolder> {

    private List<Specification> specList;
    private boolean isExpanded = false; // trạng thái đang mở rộng hay thu gọn

    public SpecAdapter(List<Specification> specList) {
        this.specList = specList;
    }
    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
        notifyDataSetChanged(); // yêu cầu adapter load lại bảng
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    @NonNull
    @Override
    public SpecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spec_row, parent, false);
        return new SpecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecViewHolder holder, int position) {
        Specification spec = specList.get(position);
        holder.tvSpecKey.setText(spec.getKey().toUpperCase());
        holder.tvSpecValue.setText(spec.getValue());
    }
    @Override
    public int getItemCount() {
        if (specList == null) return 0;
        if (isExpanded || specList.size() <= 5) {
            return specList.size();
        }
        else {
            return 5;
        }
    }

    static class SpecViewHolder extends RecyclerView.ViewHolder {
        TextView tvSpecKey, tvSpecValue;
        public SpecViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSpecKey = itemView.findViewById(R.id.tvSpecKey);
            tvSpecValue = itemView.findViewById(R.id.tvSpecValue);
        }
    }
}