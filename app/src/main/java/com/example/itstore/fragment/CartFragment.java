package com.example.itstore.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.itstore.activity.CheckoutActivity;
import com.example.itstore.adapter.CartAdapter;
import com.example.itstore.databinding.FragmentCartBinding;
import com.example.itstore.model.CartItem;
import com.example.itstore.utils.CartManager;
import com.example.itstore.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.List;
public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        setupRecyclerView();
        observeViewModel();
        binding.ivBack.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
        // SỬA TRONG FILE: fragment/CartFragment.java

        binding.btnCheckout.setOnClickListener(v -> {
            List<CartItem> itemsToBuy = new ArrayList<>();
            List<CartItem> currentCart = cartViewModel.getCartItems().getValue();

            if (currentCart != null) {
                for (CartItem item : currentCart) {
                    if (item.isSelected()) {
                        itemsToBuy.add(item);
                    }
                }
            }
            if (itemsToBuy.isEmpty()) {
                Toast.makeText(requireContext(), "Bạn chưa chọn sản phẩm nào để mua!", Toast.LENGTH_SHORT).show();
                return;
            }
            CartManager.getInstance().setCheckoutList(itemsToBuy);
            Intent intent = new Intent(requireActivity(), CheckoutActivity.class);
            startActivity(intent);
        });

        binding.cbBuyAll.setOnClickListener(v -> {
            boolean isChecked = binding.cbBuyAll.isChecked();
            cartViewModel.toggleSelectAll(isChecked);
        });

        cartViewModel.loadCartFromManager();
        List<CartItem> checkCart = CartManager.getInstance().getCartList();
        if (checkCart != null) {
            Toast.makeText(requireContext(), "Trong giỏ đang có: " + checkCart.size() + " món", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(requireContext(), "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemSelected(CartItem item, boolean isChecked) {
                item.setSelected(isChecked);
                cartViewModel.checkAllSelectedStatus();
                cartViewModel.calculateTotal();
                cartViewModel.getCartItems().setValue(currentList);
            }
        });
        binding.rvCart.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvCart.setAdapter(cartAdapter);
    }

    private void observeViewModel() {
        cartViewModel.getCartItems().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                cartAdapter.setCartList(items);
            }
            int selectedCount = 0;
            for (CartItem item : items) {
                if (item.isSelected()) selectedCount++;
            }
            binding.btnCheckout.setText("Mua hàng (" + selectedCount + ")");
        });

        cartViewModel.getTotalPrice().observe(getViewLifecycleOwner(), total -> {
            binding.tvTotalPrice.setText(String.format(java.util.Locale.US, "%,.0f đ", total));
        });
        cartViewModel.getIsAllSelectedLiveData().observe(getViewLifecycleOwner(), isAllSelected -> {
            binding.cbBuyAll.setChecked(isAllSelected);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}