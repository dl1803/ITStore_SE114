package com.example.itstore.adapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.itstore.databinding.ItemProductBinding;
import com.example.itstore.activity.ProductDetailActivity;
import com.example.itstore.model.Product;
import java.util.List;
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private Context context;
    private List<Product> productList;
    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null) return;
        holder.binding.tvName.setText(product.getName());
        holder.binding.tvPrice.setText(String.format(java.util.Locale.US, "%,.0f VNĐ", product.getPrice()));
        holder.binding.imgProduct.setImageURI(Uri.parse(product.getImageUrl()));
        holder.binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_INFO", product);
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }
    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;
        public ProductViewHolder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public void updateList(List<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }
}
