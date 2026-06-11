package com.example.itstore.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.itstore.adapter.UnreviewedAdapter;
import com.example.itstore.api.RetrofitClient;
import com.example.itstore.databinding.ActivityUnreviewedListBinding;
import com.example.itstore.model.UnreviewedResponse;
import com.example.itstore.model.UnreviewedResponse.UnreviewedItem;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnreviewedListActivity extends AppCompatActivity {

    private ActivityUnreviewedListBinding binding;
    private UnreviewedAdapter adapter;
    private final List<UnreviewedItem> unreviewedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUnreviewedListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());

        adapter = new UnreviewedAdapter(this, unreviewedItems);
        binding.rvUnreviewed.setLayoutManager(new LinearLayoutManager(this));
        binding.rvUnreviewed.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUnreviewedProducts();
    }

    private void loadUnreviewedProducts() {
        RetrofitClient.getApiService(this).getUnreviewedItems().enqueue(new Callback<UnreviewedResponse>() {
            @Override
            public void onResponse(Call<UnreviewedResponse> call, Response<UnreviewedResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    unreviewedItems.clear();
                    List<UnreviewedItem> data = response.body().getData();
                    if (data != null && !data.isEmpty()) {
                        unreviewedItems.addAll(data);
                        binding.rvUnreviewed.setVisibility(View.VISIBLE);
                        binding.layoutEmpty.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    } else {
                        binding.rvUnreviewed.setVisibility(View.GONE);
                        binding.layoutEmpty.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(UnreviewedListActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UnreviewedResponse> call, Throwable t) {
                Toast.makeText(UnreviewedListActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}