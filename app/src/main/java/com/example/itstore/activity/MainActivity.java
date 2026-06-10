package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.itstore.R;
import com.example.itstore.adapter.BannerAdapter;
import com.example.itstore.adapter.CategoryAdapter;
import com.example.itstore.adapter.ProductAdapter;
import com.example.itstore.api.RetrofitClient;
import com.example.itstore.databinding.ActivityMainBinding;
import com.example.itstore.model.NotificationResponse;
import com.example.itstore.model.TokenRegistrationRequest;
import com.example.itstore.viewmodel.HomeViewModel;
import com.example.itstore.viewmodel.NotificationViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NotificationViewModel notificationViewModel;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());


            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);
            navController = navHostFragment.getNavController();
        NavInflater inflater = navController.getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.nav_graph);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("navigate_to")) {
            String destination = intent.getStringExtra("navigate_to");
            if ("cart".equals(destination)) {
                graph.setStartDestination(R.id.nav_cart);
            } else if ("favorite".equals(destination)) {
                graph.setStartDestination(R.id.nav_favorite);
            }
            intent.removeExtra("navigate_to");
        }
        navController.setGraph(graph);
            NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.nav_search) {
                binding.bottomNavigation.setVisibility(View.GONE);
            } else {
                binding.bottomNavigation.setVisibility(View.VISIBLE);
            }
        });
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        FirebaseApp.initializeApp(this);
        registerNotificationToken();
    }
    @Override
    protected void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent != null && intent.hasExtra("navigate_to")) {
            String destination = intent.getStringExtra("navigate_to");
            try {
                if ("cart".equals(destination)) {
                    navController.navigate(R.id.nav_cart);
                } else if ("favorite".equals(destination)) {
                    navController.navigate(R.id.nav_favorite);
                }
            } catch (Exception e) {
                Log.e("Loi_Chuyen_Trang", "Lỗi ở onNewIntent: " + e.getMessage());
            }
            intent.removeExtra("navigate_to");
        }
    }
    private void registerNotificationToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) return;
                    String token = task.getResult();
                    notificationViewModel.registerFCMToken(token);
                });
    }

}
