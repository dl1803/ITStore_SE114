package com.example.itstore.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.itstore.activity.LoginActivity;
import com.example.itstore.activity.ProductDetailActivity;
import com.example.itstore.adapter.BannerAdapter;
import com.example.itstore.adapter.BrandFilterAdapter;
import com.example.itstore.adapter.CategoryAdapter;
import com.example.itstore.adapter.ProductAdapter;
import com.example.itstore.api.RetrofitClient;
import com.example.itstore.databinding.FragmentHomeBinding;
import com.example.itstore.model.Category;
import com.example.itstore.model.CategoryResponse;
import com.example.itstore.model.MockDataRepository;
import com.example.itstore.model.Product;
import com.example.itstore.utils.SharedPrefsManager;
import com.example.itstore.viewmodel.HomeViewModel;
import com.example.itstore.viewmodel.ProductDetailViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private final ActivityResultLauncher<Intent> detailLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                            Product updatedProduct = (Product) result.getData().getSerializableExtra("UPDATED_PRODUCT");
                            if (updatedProduct != null && homeViewModel != null) {
                                homeViewModel.updateProduct(updatedProduct);
                            }
                        }
                    }
            );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
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
            productAdapter = new ProductAdapter(requireContext(), products, new ProductAdapter.OnProductInteractionListener() {

                // Chuyển sang màn hình chi tiết
                @Override
                public void onProductClick(Product product) {
                    Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
                    intent.putExtra("PRODUCT_INFO", product);
                    detailLauncher.launch(intent);
                }

                // Nút tim
                @Override
                public void onFavoriteClick(Product product, int position) {
                    String token = SharedPrefsManager.getInstance(requireContext()).getAccessToken();
                    if (token == null || token.isEmpty()) {
                        Toast.makeText(requireContext(), "Vui lòng đăng nhập để lưu yêu thích!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(requireContext(), LoginActivity.class));
                    } else {
                        boolean newStatus = !product.isFavorite();
                        product.setFavorite(newStatus);
                        MockDataRepository.getInstance().updateProduct(product);
                        productAdapter.notifyItemChanged(position);
                        Toast.makeText(requireContext(), newStatus ? "Đã thêm vào yêu thích" : "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
                    }
                }

                // Nút thêm vào giỏ hàng
                @Override
                public void onAddToCartClick(Product product) {
                    String token = SharedPrefsManager.getInstance(requireContext()).getAccessToken();
                    if (token == null || token.isEmpty()) {
                        Toast.makeText(requireContext(), "Vui lòng đăng nhập để thêm vào giỏ!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(requireContext(), LoginActivity.class));
                        return;
                    }

                    if (product.getVariants() != null && product.getVariants().size() > 1) {
                        // Nhiều phiên bản thì qua chi tiết sản phẩm chọn
                        Toast.makeText(requireContext(), "Vui lòng chọn phiên bản bạn muốn mua", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
                        intent.putExtra("PRODUCT_INFO", product);
                        detailLauncher.launch(intent);
                    } else {
                        ProductDetailViewModel detailViewModel = new ViewModelProvider(requireActivity()).get(ProductDetailViewModel.class);

                        int defaultVariantId = 1;
                        String defaultVariantName = "Mặc định";
                        if (product.getVariants() != null && !product.getVariants().isEmpty()) {
                            defaultVariantId = product.getVariants().get(0).getId();
                            defaultVariantName = product.getVariants().get(0).getVersion();
                        }

                        detailViewModel.addToCart(product, defaultVariantId, defaultVariantName, product.getPrice());
                        Toast.makeText(requireContext(), "Đã thêm " + product.getName() + " vào giỏ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            rcvProducts.setAdapter(productAdapter);
        });

        categoryAdapter = new CategoryAdapter(requireContext(), new ArrayList<>(), new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(Category category) {
                homeViewModel.filterByCategory(category.getId());
            }
        });
        rcvCategories = binding.recyclerCategory;
        rcvCategories.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        rcvCategories.setAdapter(categoryAdapter);

        homeViewModel.getCategoryListLiveData().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                categoryAdapter.updateData(categories);
            }
        });

        // lấy data từ api
        homeViewModel.fetchCategories(requireContext());

        binding.tvSeeAll.setOnClickListener(v -> {
            androidx.navigation.Navigation.findNavController(v).navigate(R.id.nav_search);
        });
        binding.layoutSearchBar.setOnClickListener(v -> {
            androidx.navigation.Navigation.findNavController(v).navigate(R.id.nav_search);
        });
        return view;

    }
    @Override
    public void onResume() {
        super.onResume();
        if (productAdapter != null) {
            List<Product> freshProducts = MockDataRepository.getInstance().getAllProducts();
            productAdapter.updateList(freshProducts);
        }
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