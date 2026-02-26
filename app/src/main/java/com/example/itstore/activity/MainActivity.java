package com.example.itstore.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.itstore.R;
import com.example.itstore.adapter.BannerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        BannerAdapter && Indicators (...)
        ViewPager2 viewPager2 =  findViewById(R.id.viewPagerBanner);

        List<Integer> listBanner = new ArrayList<>();
        listBanner.add(R.drawable.banner1);
        listBanner.add(R.drawable.banner2);
        listBanner.add(R.drawable.banner3);

        BannerAdapter bannerAdapter = new BannerAdapter(listBanner);
        viewPager2.setAdapter(bannerAdapter);

        Handler handler =  new Handler(Looper.getMainLooper());

        Runnable runBanner = new Runnable() {
            @Override
            public void run() {
                int curItem = viewPager2.getCurrentItem();
                int totalItem = listBanner.size() - 1;

                if (curItem < totalItem) {
                    curItem++;
                    viewPager2.setCurrentItem(curItem);
                } else {
                    viewPager2.setCurrentItem(0);
                }

            }
        };
        android.widget.LinearLayout layoutIndicators = findViewById(R.id.layoutIndicators);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (layoutIndicators != null) {
                    for (int i = 0; i < layoutIndicators.getChildCount(); i++) {
                        androidx.cardview.widget.CardView dot = (androidx.cardview.widget.CardView) layoutIndicators.getChildAt(i);

                        if (i == position) {
                            dot.setCardBackgroundColor(android.graphics.Color.parseColor("#FF9800"));
                            android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) dot.getLayoutParams();
                            params.width = 60;
                            params.height = 20;
                            dot.setLayoutParams(params);
                        }
                        else {
                            dot.setCardBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"));
                            android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) dot.getLayoutParams();
                            params.width = 20;
                            params.height = 20;
                            dot.setLayoutParams(params);
                        }
                    }
                }
                handler.removeCallbacks(runBanner);
                handler.postDelayed(runBanner, 3000);
            }
        });
        // End of BannerAdapter && Indicators (...)
    }
}