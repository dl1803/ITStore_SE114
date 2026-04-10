package com.example.itstore.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
    private int currentVariantId = 1;
    private String currentRam = "8GB";
    private double currentFinalPrice;

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
        double basePrice = currentProduct.getPrice();
        double baseOldPrice = currentProduct.getCompareAtPrice();
        currentFinalPrice = basePrice;
        binding.tvPriceOld.setPaintFlags(binding.tvPriceOld.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        binding.tvProductName.setText(currentProduct.getName());
        updatePriceDisplay(basePrice, baseOldPrice);
        List<Integer> listImages = new ArrayList<>();
        listImages.add(R.drawable.ram1);
        ImagePagerAdapter imageAdapter = new ImagePagerAdapter(listImages);
        binding.imgProductDetail.setAdapter(imageAdapter);
        updateFavoriteIcon(currentProduct.isFavorite());

        binding.ivBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        binding.ivCart.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.nav_cart));

        binding.cardChoice1.setOnClickListener(v -> {
            binding.cardChoice1.setBackgroundResource(R.color.orange_primary);
            binding.cardChoice1.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.cardChoice2.setBackgroundResource(android.R.color.transparent);
            binding.cardChoice2.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_gray));
            updatePriceDisplay(basePrice, baseOldPrice);
            currentVariantId = 1;
            currentRam = "8GB";
            currentFinalPrice = basePrice;
        });

        binding.cardChoice2.setOnClickListener(v -> {
            binding.cardChoice2.setBackgroundResource(R.color.orange_primary);
            binding.cardChoice2.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.cardChoice1.setBackgroundResource(android.R.color.transparent);
            binding.cardChoice1.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_gray));
            updatePriceDisplay(basePrice + 500000, baseOldPrice + 500000);
            currentVariantId = 2;
            currentRam = "16GB";
            currentFinalPrice = basePrice + 500000;
        });

        binding.btnAddToCart.setOnClickListener(v -> {
            detailViewModel.addToCart(currentProduct, currentVariantId, currentRam, currentFinalPrice);
            Toast.makeText(requireContext(), "Đã thêm bản " + currentRam + " vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });

        binding.imgFavoriteItem.setOnClickListener(v -> {
            boolean newStatus = !currentProduct.isFavorite();
            currentProduct.setFavorite(newStatus);
            homeViewModel.updateProduct(currentProduct);
            updateFavoriteIcon(newStatus);
            Toast.makeText(requireContext(), newStatus ? "Đã thêm vào danh sách yêu thích" : "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
        });
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