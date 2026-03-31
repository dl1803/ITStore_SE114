package com.example.itstore.activity;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.itstore.adapter.CartAdapter;
import com.example.itstore.databinding.ActivityCartBinding;
import com.example.itstore.model.CartItem;
import com.example.itstore.utils.CartManager;
import com.example.itstore.viewmodel.CartViewModel;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity{
    private ActivityCartBinding binding;
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        setupRecyclerView();
        observeViewModel();
        binding.ivBack.setOnClickListener(v -> finish());
        binding.btnCheckout.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng Thanh toán đang phát triển!", Toast.LENGTH_SHORT).show();
        });

        binding.cbBuyAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartViewModel.toggleSelectAll(isChecked);
        });

        cartViewModel.loadCartFromManager();
        List<CartItem> checkCart = CartManager.getInstance().getCartList();
        if (checkCart != null) {
            Toast.makeText(this, "Trong giỏ đang có: " + checkCart.size() + " món", Toast.LENGTH_SHORT).show();
        }
    }
    private void setupRecyclerView() {
        List<CartItem> currentList = CartManager.getInstance().getCartList();
        cartAdapter = new CartAdapter(new ArrayList<>(), new CartAdapter.CartClickListener() {
            @Override
            public void onIncrease(CartItem item, int position) {
                cartViewModel.increaseQuantity(item, position);
            }
            @Override
            public void onDecrease(CartItem item, int position) {
                cartViewModel.decreaseQuantity(item, position);
            }

            @Override
            public void onDelete(CartItem item, int position) {
                cartViewModel.removeItem(position);
                Toast.makeText(CartActivity.this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemSelected(CartItem item, boolean isChecked) {
                cartViewModel.calculateTotal();
                cartViewModel.getCartItems().setValue(currentList);
            }
        });
        binding.rvCart.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCart.setAdapter(cartAdapter);
    }
    private void observeViewModel() {
        cartViewModel.getCartItems().observe(this, items -> {
            if (items != null) {
                cartAdapter.setCartList(items);
            }
            int selectedCount = 0;
            for (CartItem item : items) {
                if (item.isSelected()) selectedCount++;
            }
            binding.btnCheckout.setText("Mua hàng (" + selectedCount + ")");
        });
        cartViewModel.getTotalPrice().observe(this, total -> {
            binding.tvTotalPrice.setText(String.format(java.util.Locale.US, "%,.0f đ", total));
        });
    }
}
