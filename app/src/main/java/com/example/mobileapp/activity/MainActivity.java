package com.example.mobileapp.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mobileapp.R;
import com.example.mobileapp.fragment.AccountFragment;
import com.example.mobileapp.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    /**
     * Nguyễn Tuấn Kiệt
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_profile) {
                // Khi chạm vào icon Tài khoản, mở fragment AccountFragment
                selectedFragment = new AccountFragment();
            } else if (itemId == R.id.nav_cart) {
                Toast.makeText(this, "Chức năng Giỏ hàng", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.nav_store) {
                Toast.makeText(this, "Chức năng Cửa hàng đang phát triển", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.nav_offers) {
                Toast.makeText(this, "Chức năng Ưu đãi đang phát triển", Toast.LENGTH_SHORT).show();
            }


            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });
    }
}
