package com.example.itstore.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itstore.databinding.ItemProductCartBinding;
import com.example.itstore.model.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefundItemAdapter extends RecyclerView.Adapter<RefundItemAdapter.ViewHolder> {
    private List<Product> list;
    private Map<Integer, Integer> quantityMap = new HashMap<>();
    private Map<Integer, Boolean> checkedMap = new HashMap<>();
    private OnRefundAmountChangeListener listener;

    public interface OnRefundAmountChangeListener {
        void onAmountChanged(double totalRefund, int selectedCount);
    }

    public RefundItemAdapter(List<Product> list, OnRefundAmountChangeListener listener) {
        this.list = list;
        this.listener = listener;
        if (list != null) {
            for (Product p : list) {
                quantityMap.put(p.getId(), 1);
                checkedMap.put(p.getId(), false);
            }
        }
    }

    private void calculateTotal() {
        double total = 0;
        int count = 0;
        for (Product p : list) {
            if (checkedMap.get(p.getId()) != null && checkedMap.get(p.getId())) {
                total += p.getPrice() * quantityMap.get(p.getId());
                count++;
            }
        }
        if (listener != null) {
            listener.onAmountChanged(total, count);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductCartBinding binding = ItemProductCartBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = list.get(position);
        int productId = product.getId();

        holder.binding.tvProductName.setText(product.getName());
        holder.binding.tvPrice.setText(String.format(java.util.Locale.US, "%,.0f đ", product.getPrice()));

        holder.binding.tvQuantity.setText(String.valueOf(quantityMap.get(productId)));

        holder.binding.cbAgreeBuy.setOnCheckedChangeListener(null);
        holder.binding.cbAgreeBuy.setChecked(checkedMap.get(productId));

        holder.binding.cbAgreeBuy.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkedMap.put(productId, isChecked);
            calculateTotal();
        });

        holder.binding.ivPlus.setOnClickListener(v -> {
            int currentQty = quantityMap.get(productId);
            currentQty++;
            quantityMap.put(productId, currentQty);
            holder.binding.tvQuantity.setText(String.valueOf(currentQty));
            calculateTotal();
        });

        holder.binding.ivMinus.setOnClickListener(v -> {
            int currentQty = quantityMap.get(productId);
            if (currentQty > 1) {
                currentQty--;
                quantityMap.put(productId, currentQty);
                holder.binding.tvQuantity.setText(String.valueOf(currentQty));
                calculateTotal();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public Map<Integer, Integer> getSelectedItems() {
        Map<Integer, Integer> result = new HashMap<>();
        for (Product p : list) {
            if (checkedMap.get(p.getId()) != null && checkedMap.get(p.getId())) {
                result.put(p.getId(), quantityMap.get(p.getId()));
            }
        }
        return result;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemProductCartBinding binding;
        public ViewHolder(ItemProductCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}