package com.example.itstore.fragment;

import android.content.Context;
import android.content.Intent;
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
import com.example.itstore.activity.ProductDetailActivity;
import com.example.itstore.adapter.ProductAdapter;
import com.example.itstore.databinding.FragmentSearchBinding;
import com.example.itstore.model.Product;
import com.example.itstore.viewmodel.SearchViewModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private ProductAdapter productAdapter;

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
        });
        binding.edtSearch.requestFocus();
        binding.edtSearch.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                // Dùng SHOW_IMPLICIT là an toàn nhất, tự nó sẽ biết bung hay không
                imm.showSoftInput(binding.edtSearch, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);
        binding.btnBack.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            Navigation.findNavController(v).popBackStack();
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                String categoryName = getSelectedCategoryName();
                viewModel.filterProducts(query, categoryName);
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
        binding.chipGroupFilter.setOnCheckedStateChangeListener((group, checkedIds) -> {
            String query = binding.edtSearch.getText().toString().trim();
            String categoryName = getSelectedCategoryName();
            viewModel.filterProducts(query, categoryName);
        });
    }
    private String getSelectedCategoryName() {
        int selectedChipId = binding.chipGroupFilter.getCheckedChipId();
        if (selectedChipId == View.NO_ID) {
            return "Tất cả";
        }
        Chip selectedChip = binding.chipGroupFilter.findViewById(selectedChipId);
        if (selectedChip != null) {
            return selectedChip.getText().toString();
        }
        return "Tất cả";
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}