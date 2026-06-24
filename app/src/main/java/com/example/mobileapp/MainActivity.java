package com.example.mobileapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mobileapp.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kiểm tra xem đây có phải lần đầu tiên mở App không (đề phòng bấm xoay màn hình bị nạp trùng)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
    }
}