package com.example.itstore.adapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.itstore.databinding.ItemProductCartBinding;
import com.example.itstore.model.CartItem;
import java.util.List;
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private List<CartItem> cartList;
    private CartClickListener listener;
    public interface CartClickListener {
        void onIncrease(CartItem item, int position);
        void onDecrease(CartItem item, int position);
        void onDelete(CartItem item, int position);
        void onItemSelected(CartItem item, boolean isChecked);
    }

    public CartAdapter(List<CartItem> cartList, CartClickListener listener) {
        this.cartList = cartList;
        this.listener = listener;
    }

    public void setCartList(List<CartItem> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductCartBinding binding = ItemProductCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartList.get(position);
        if (item == null) return;
        holder.binding.tvProductName.setText(item.getProduct().getName());
        holder.binding.tvVariant.setText(item.getVariantName());
        holder.binding.tvPrice.setText(String.format(java.util.Locale.US, "%,.0f đ", item.getPrice()));
        holder.binding.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.binding.ivPlus.setOnClickListener(v -> {
            if (listener != null) listener.onIncrease(item, position);
        });
        holder.binding.ivMinus.setOnClickListener(v -> {
            if (listener != null) listener.onDecrease(item, position);
        });
        holder.binding.ivDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(item, position);
        });
        holder.binding.cbAgreeBuy.setOnCheckedChangeListener(null);
        holder.binding.cbAgreeBuy.setChecked(item.isSelected());
        holder.binding.cbAgreeBuy.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected(isChecked);
            if (listener != null) {
                listener.onItemSelected(item, isChecked);
            }
        });
    }
    @Override
    public int getItemCount() {
        return cartList != null ? cartList.size() : 0;
    }
    public class CartViewHolder extends RecyclerView.ViewHolder {
        ItemProductCartBinding binding;
        public CartViewHolder(@NonNull ItemProductCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
