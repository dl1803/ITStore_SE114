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
                if (s.length() == 0) {
                    binding.tvResultRecommend.setText("Gợi ý tìm kiếm");
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
        binding.ivSearch.setOnClickListener(v -> {
            String query = binding.edtSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                binding.tvResultRecommend.setText("Kết quả tìm kiếm");
                viewModel.filterProducts(query, "Tất cả");
                hideKeyboard(v);
            }
        });
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