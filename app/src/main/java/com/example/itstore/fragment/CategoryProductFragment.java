package com.example.itstore.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.itstore.adapter.ProductAdapter;
import com.example.itstore.databinding.FragmentCategoryProductBinding;
import com.example.itstore.dialog.FilterProductDialog;
import com.example.itstore.model.Product;
import com.example.itstore.viewmodel.HomeViewModel;
import java.util.ArrayList;
import java.util.List;

public class CategoryProductFragment extends Fragment {
    private FragmentCategoryProductBinding binding;
    private HomeViewModel homeViewModel;
    private ProductAdapter productAdapter;
    private String selectedCategoryId = "all";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoryProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        if (getArguments() != null) {
            selectedCategoryId = getArguments().getString("CATEGORY_ID", "all");
            String categoryName = getArguments().getString("CATEGORY_NAME", "Danh mục");
            binding.tvCategoryTitle.setText(categoryName);
        }
        productAdapter = new ProductAdapter(requireContext(), new ArrayList<>());
        binding.rvCategoryProduct.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvCategoryProduct.setAdapter(productAdapter);
        applyFilters(0, Double.MAX_VALUE);
        binding.btnFilter.setOnClickListener(v -> {
            openFilterDialog();
        });
        binding.btnBack.setOnClickListener(v -> {
            androidx.navigation.Navigation.findNavController(v).popBackStack();
        });
    }

    private void openFilterDialog() {
        FilterProductDialog dialog = new FilterProductDialog();
        dialog.setOnFilterAppliedListener((min, max, brand) -> {
            applyFilters(min, max);
        });
        dialog.show(getChildFragmentManager(), "FilterProductDialog");
    }
    private void applyFilters(double minPrice, double maxPrice) {
        homeViewModel.getProductListLiveData().observe(getViewLifecycleOwner(), products -> {
            if (products == null) return;
            List<Product> filteredList = new ArrayList<>();
            for (Product p : products) {
                String productIdStr = String.valueOf(p.getCategoryId());
                boolean matchCategory = selectedCategoryId.equals("all") || productIdStr.equals(selectedCategoryId);
                double currentPrice = p.getPrice();
                boolean matchPrice = currentPrice >= minPrice && currentPrice <= maxPrice;
                if (matchCategory && matchPrice) {
                    filteredList.add(p);
                }
            }
            productAdapter.updateList(filteredList);
            binding.tvItemCount.setText("Tìm thấy " + filteredList.size() + " sản phẩm");
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
