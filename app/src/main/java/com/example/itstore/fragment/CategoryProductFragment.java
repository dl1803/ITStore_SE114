package com.example.itstore.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.itstore.adapter.ProductAdapter;
import com.example.itstore.api.RetrofitClient;
import com.example.itstore.databinding.FragmentCategoryProductBinding;
import com.example.itstore.dialog.FilterProductDialog;
import com.example.itstore.model.Product;
import com.example.itstore.model.ProductResponse;
import com.example.itstore.viewmodel.HomeViewModel;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryProductFragment extends Fragment {
    private FragmentCategoryProductBinding binding;
    private HomeViewModel homeViewModel;
    private ProductAdapter productAdapter;
    private String selectedCategoryId = "all";
    private String currentSort = "";
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
            Navigation.findNavController(v).popBackStack();
        });
        binding.tvSort.setOnClickListener(v -> {
            showSortMenu();
        });
        loadProductsByCategory();
    }

    private void showSortMenu() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), binding.tvSort);

        popupMenu.getMenu().add(0, 1, 0, "Mặc định");
        popupMenu.getMenu().add(0, 2, 0, "Mới nhất");
        popupMenu.getMenu().add(0, 3, 0, "Cũ nhất");

        popupMenu.setOnMenuItemClickListener(item -> {
            String selectedTitle = item.getTitle().toString();

            binding.tvSort.setText("Sắp xếp: " + selectedTitle);

            switch (item.getItemId()) {
                case 2:
                    currentSort = "newest";
                    break;
                case 3:
                    currentSort = "oldest";
                    break;
                default:
                    currentSort = "";
                    break;
            }
            loadProductsByCategory();

            return true;
        });

        popupMenu.show();
    }
    private void loadProductsByCategory() {
        int categoryId = getArguments() != null ? Integer.parseInt(getArguments().getString("CATEGORY_ID", "-1")) : -1;

        RetrofitClient.getApiService(requireContext()).getProducts(
                1, 20, null, categoryId, null, null, null,
                currentSort.isEmpty() ? null : currentSort).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    productAdapter.updateList(response.body().getData());
                    binding.tvItemCount.setText("Tìm thấy " + response.body().getPagination().getTotal() + " sản phẩm");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e("LỖI_API", "Không tải được sản phẩm danh mục: " + t.getMessage());
            }
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
