package com.example.itstore.fragment;

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

import com.example.itstore.R;
import com.example.itstore.databinding.FragmentProductDetailBinding;
import com.example.itstore.adapter.ImagePagerAdapter;
import com.example.itstore.model.Product;
import com.example.itstore.viewmodel.HomeViewModel;
import com.example.itstore.viewmodel.ProductDetailViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductDetailFragment extends Fragment {
    private FragmentProductDetailBinding binding;
    private Product currentProduct;
    private HomeViewModel homeViewModel;
    private ProductDetailViewModel detailViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        detailViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        if (getArguments() != null) {
            currentProduct = (Product) getArguments().getSerializable("PRODUCT_INFO");
        }
        if (currentProduct == null) {
            Toast.makeText(requireContext(), "Lỗi: Không tải được thông tin sản phẩm!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
            return;
        }
        binding.btnAddToCart.setVisibility(View.VISIBLE);
        binding.btnBuyNow.setVisibility(View.VISIBLE);
        setupUI();
        setupEventListeners();
    }

    private void setupUI() {
        binding.tvPriceOld.setPaintFlags(binding.tvPriceOld.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        binding.tvProductName.setText(currentProduct.getName());
        updatePriceDisplay(currentProduct.getPrice(), currentProduct.getCompareAtPrice());
        List<Integer> listImages = new ArrayList<>();
        listImages.add(R.drawable.ram1);
        ImagePagerAdapter imageAdapter = new ImagePagerAdapter(listImages);
        binding.imgProductDetail.setAdapter(imageAdapter);
        updateFavoriteIcon(currentProduct.isFavorite());
    }

    private void setupEventListeners() {
        binding.ivBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        binding.cardChoice1.setOnClickListener(v -> {
            updateChoiceUI(true);
            updatePriceDisplay(currentProduct.getPrice(), currentProduct.getCompareAtPrice());
        });
        binding.cardChoice2.setOnClickListener(v -> {
            updateChoiceUI(false);
            updatePriceDisplay(currentProduct.getPrice() + 500000, currentProduct.getCompareAtPrice() + 500000);
        });
        binding.btnAddToCart.setOnClickListener(v -> {
            detailViewModel.addToCart(currentProduct, 1, "8GB", currentProduct.getPrice());
            Toast.makeText(requireContext(), "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });
        binding.imgFavoriteItem.setOnClickListener(v -> {
            boolean newStatus = !currentProduct.isFavorite();
            currentProduct.setFavorite(newStatus);
            homeViewModel.updateProduct(currentProduct);
            updateFavoriteIcon(newStatus);
            String msg = newStatus ? "Đã thêm vào yêu thích" : "Đã xóa khỏi yêu thích";
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
        });
    }
    private void updateChoiceUI(boolean isChoice1) {
        int orange = androidx.core.content.ContextCompat.getColor(requireContext(), R.color.orange_primary);
        int white = androidx.core.content.ContextCompat.getColor(requireContext(), R.color.white);
        int gray = androidx.core.content.ContextCompat.getColor(requireContext(), R.color.dark_gray);
        binding.cardChoice1.setBackgroundResource(isChoice1 ? R.color.orange_primary : android.R.color.transparent);
        binding.cardChoice1.setTextColor(isChoice1 ? white : gray);
        binding.cardChoice2.setBackgroundResource(!isChoice1 ? R.color.orange_primary : android.R.color.transparent);
        binding.cardChoice2.setTextColor(!isChoice1 ? white : gray);
    }

    private void updateFavoriteIcon(boolean isFav) {
        int color = isFav ? android.graphics.Color.parseColor("#FF9800") : android.graphics.Color.parseColor("#B3B3B3");
        binding.imgFavoriteItem.setColorFilter(color);
    }

    private void updatePriceDisplay(double newPrice, double oldPrice) {
        binding.tvPriceNew.setText(String.format(Locale.US, "%,.0f VNĐ", newPrice));
        binding.tvPriceOld.setText(String.format(Locale.US, "%,.0f VNĐ", oldPrice));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}