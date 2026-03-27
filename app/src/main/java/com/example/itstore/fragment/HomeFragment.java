package com.example.itstore.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.itstore.R;
import com.example.itstore.activity.CartActivity;
import com.example.itstore.adapter.BannerAdapter;
import com.example.itstore.adapter.CategoryAdapter;
import com.example.itstore.adapter.ProductAdapter;
import com.example.itstore.databinding.FragmentHomeBinding;
import com.example.itstore.model.Category;
import com.example.itstore.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private RecyclerView rcvProducts;
    private ProductAdapter productAdapter;
    private RecyclerView rcvCategories;
    private CategoryAdapter categoryAdapter;

    private Handler handler;
    private Runnable runBanner;

    private LinearLayout layoutIndicators;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        layoutIndicators = binding.layoutIndicators;

        ViewPager2 viewPager2 = binding.viewPagerBanner;
        List<Integer> listBanner = new ArrayList<>();
        listBanner.add(R.drawable.banner1);
        listBanner.add(R.drawable.banner2);
        listBanner.add(R.drawable.banner3);

        BannerAdapter bannerAdapter = new BannerAdapter(listBanner);
        viewPager2.setAdapter(bannerAdapter);

        handler = new Handler(Looper.getMainLooper());
        runBanner = () -> {
            int curItem = viewPager2.getCurrentItem();
            int totalItem = listBanner.size() - 1;
            if (curItem < totalItem) {
                viewPager2.setCurrentItem(curItem + 1);
            } else {
                viewPager2.setCurrentItem(0);
            }
        };

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (layoutIndicators != null) {
                    for (int i = 0; i < layoutIndicators.getChildCount(); i++) {
                        CardView dot = (CardView) layoutIndicators.getChildAt(i);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dot.getLayoutParams();

                        if (i == position) {
                            dot.setCardBackgroundColor(android.graphics.Color.parseColor("#FF9800"));
                            params.width = 60;
                            params.height = 20;
                        } else {
                            dot.setCardBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"));
                            params.width = 20;
                            params.height = 20;
                        }
                        dot.setLayoutParams(params);
                    }
                }

                handler.removeCallbacks(runBanner);
                handler.postDelayed(runBanner, 3000);
            }
        });


        rcvProducts = binding.recyclerProduct;
        rcvProducts.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        homeViewModel.getProductListLiveData().observe(getViewLifecycleOwner(), products -> {
            productAdapter = new ProductAdapter(requireContext(), products);
            rcvProducts.setAdapter(productAdapter);
        });

        rcvCategories = binding.recyclerCategory;
        rcvCategories.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        homeViewModel.getCategoryListLiveData().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter = new CategoryAdapter(requireContext(), categories, new CategoryAdapter.OnCategoryClickListener() {
                @Override
                public void onCategoryClick(Category category) {
                    homeViewModel.filterByCategory(category.getId());
                }
            });
            rcvCategories.setAdapter(categoryAdapter);
        });
        binding.tvSeeAll.setOnClickListener(v -> {
            androidx.navigation.Navigation.findNavController(v).navigate(R.id.nav_search);
        });
        binding.layoutSearchBar.setOnClickListener(v -> {
            androidx.navigation.Navigation.findNavController(v).navigate(R.id.nav_search);
        });
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null && runBanner != null) {
            handler.removeCallbacks(runBanner);
        }
        binding = null;
    }
}