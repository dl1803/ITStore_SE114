package com.example.itstore.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.itstore.R;
import com.example.itstore.adapter.FavoriteAdapter;
import com.example.itstore.databinding.FragmentFavoriteBinding;
import com.example.itstore.model.Product;
import com.example.itstore.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private FragmentFavoriteBinding binding;
    private FavoriteAdapter adapter;
    private HomeViewModel homeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        FavoriteAdapter.OnFavoriteClickListener favoriteListener = new FavoriteAdapter.OnFavoriteClickListener() {
            @Override
            public void onRemoveFavorite(Product product) {
                product.setFavorite(false);
                homeViewModel.updateProduct(product);
                Toast.makeText(requireContext(), "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAddToCart(Product product) {
                android.widget.Toast.makeText(requireContext(), "Đã thêm " + product.getName() + " vào giỏ", android.widget.Toast.LENGTH_SHORT).show();
            }
        };
        adapter = new FavoriteAdapter(requireContext(), new ArrayList<>(), favoriteListener);
        binding.rvFavorite.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFavorite.setAdapter(adapter);
        homeViewModel.getProductListLiveData().observe(getViewLifecycleOwner(), products -> {
            List<Product> favList = new ArrayList<>();
            for (Product p : products) {
                if (p.isFavorite()) {
                    favList.add(p);
                }
            }
            adapter.updateList(favList);
        });
        binding.imgBack.setOnClickListener(v ->
                androidx.navigation.Navigation.findNavController(v).popBackStack());
    }
}