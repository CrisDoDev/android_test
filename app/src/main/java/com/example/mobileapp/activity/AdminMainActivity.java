package com.example.mobileapp.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.mobileapp.R;
import com.example.mobileapp.fragment.AdminHomeAnalyticsFragment;

public class AdminMainActivity extends AppCompatActivity {

    private Toolbar adminToolbar;

    /**
     * Nguyễn Tuấn Kiệt
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        adminToolbar = findViewById(R.id.adminToolbar);
        setSupportActionBar(adminToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Phân Tích Kinh Doanh");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_admin_container, new AdminHomeAnalyticsFragment()).commit();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(backStackCount > 0);
            }
        });
    }

    /**
     * Nguyễn Tuấn Kiệt
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }
}