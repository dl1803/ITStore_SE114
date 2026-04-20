package com.example.itstore.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.itstore.R;
import com.example.itstore.adapter.ProductAdapter;
import com.example.itstore.databinding.FragmentSearchBinding;
import com.example.itstore.dialog.FilterProductDialog;
import com.example.itstore.viewmodel.SearchViewModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private ProductAdapter productAdapter;
    private String currentCategory = "Tất cả";
    private double currentMinPrice = 0;
    private double currentMaxPrice = Double.MAX_VALUE;
    private List<String> currentBrands = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        productAdapter = new ProductAdapter(requireContext(), new ArrayList<>());
        binding.rvSearchRecommend.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvSearchRecommend.setAdapter(productAdapter);

        viewModel.searchResults.observe(getViewLifecycleOwner(), products -> {
            productAdapter.updateList(products);
            binding.tvResultRecommend.setText("Tìm thấy " + products.size() + " sản phẩm");
        });

        // Bật bàn phím khi mới vào
        binding.edtSearch.requestFocus();
        binding.edtSearch.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.showSoftInput(binding.edtSearch, InputMethodManager.SHOW_IMPLICIT);
        }, 200);

        binding.btnBack.setOnClickListener(v -> {
            hideKeyboard(view);
            Navigation.findNavController(v).popBackStack();
        });

        // KHI BẤM NÚT TÌM KIẾM BẰNG TỪ KHÓA
        binding.ivSearch.setOnClickListener(v -> {
            performCombinedSearch();
            hideKeyboard(v);
        });

        // KHI BẤM VÀO CHIP -> LỌC NHANH DANH MỤC
        binding.chipGroupFilter.setOnCheckedStateChangeListener((group, checkedIds) -> {
            int selectedChipId = group.getCheckedChipId();
            if (selectedChipId != View.NO_ID) {
                Chip selectedChip = group.findViewById(selectedChipId);
                String categoryId = selectedChip.getTag().toString();
                String categoryName = selectedChip.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("CATEGORY_ID", categoryId);
                bundle.putString("CATEGORY_NAME", categoryName);
                Navigation.findNavController(requireView()).navigate(R.id.action_nav_search_to_nav_category_product, bundle);
                group.clearCheck();
            }
        });

        // KHI BẤM NÚT MỞ DIALOG LỌC
        binding.btnOpenFilter.setOnClickListener(v -> {
            FilterProductDialog dialog = new FilterProductDialog();
            dialog.setOnFilterAppliedListener((min, max, brand) -> {
                currentMinPrice = min;
                currentMaxPrice = max;
                currentBrands = brand;

                performCombinedSearch(); // Gọi hàm lọc
            });

            dialog.show(getChildFragmentManager(), "FilterProductDialog");
            hideKeyboard(v);
        });
    }
    // Hàm lọc
    private void performCombinedSearch() {
        String query = binding.edtSearch.getText().toString().trim();
        if (!query.isEmpty() || !currentCategory.equals("Tất cả") || currentMinPrice > 0 || !currentBrands.isEmpty()) {
            binding.tvResultRecommend.setText("Kết quả lọc");
            // Bấm vào nút tìm kiếm thì hiện nút lọc
            binding.btnOpenFilter.setVisibility(View.VISIBLE);
        } else {
            binding.tvResultRecommend.setText("Gợi ý tìm kiếm");
            // Không bấm vào thì k hiện nút lọc
            binding.btnOpenFilter.setVisibility(View.GONE);
        }
        viewModel.filterProducts(query, currentCategory, currentMinPrice, currentMaxPrice, currentBrands);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}