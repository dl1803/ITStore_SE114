package com.example.itstore.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.itstore.adapter.SpecAdapter;
import com.example.itstore.databinding.DialogProductSpecsBinding;
import com.example.itstore.model.Product;
import com.example.itstore.model.Specification;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class SpecsBottomSheet extends BottomSheetDialogFragment {

    private Product currentProduct;
    private DialogProductSpecsBinding binding;
    public SpecsBottomSheet(Product product) {
        this.currentProduct = product;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogProductSpecsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Specification> specs = new ArrayList<>();
        specs.add(new Specification("Thương hiệu", "Kingston"));
        specs.add(new Specification("Dung lượng", "16GB (1x16GB)"));
        specs.add(new Specification("Loại RAM", "DDR4 Desktop"));
        specs.add(new Specification("Bus RAM", "3200MHz"));
        specs.add(new Specification("Độ trễ (Cas)", "CL16"));
        specs.add(new Specification("Điện áp", "1.35V"));
        specs.add(new Specification("Tản nhiệt", "Nhôm đen"));

        //Setup RecyclerView
        SpecAdapter specAdapter = new SpecAdapter(specs);
        binding.rvProductSpecs.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvProductSpecs.setAdapter(specAdapter);

        if (specs.size() > 5) {
            binding.lineDivider.setVisibility(View.VISIBLE);
            binding.btnToggleSpecs.setVisibility(View.VISIBLE);
        } else {
            binding.lineDivider.setVisibility(View.GONE);
            binding.btnToggleSpecs.setVisibility(View.GONE);
        }

        // Bắt sự kiện bấm nút xem tất cả hoặc thu vào
        binding.btnToggleSpecs.setOnClickListener(v -> {
            boolean currentExpanded = specAdapter.isExpanded();
            specAdapter.setExpanded(!currentExpanded);
            if (specAdapter.isExpanded()) {
                binding.btnToggleSpecs.setText("Thu gọn ˄");
            } else {
                binding.btnToggleSpecs.setText("Xem tất cả ˅");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}