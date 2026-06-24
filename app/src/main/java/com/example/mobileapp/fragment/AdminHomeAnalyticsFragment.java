package com.example.mobileapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.mobileapp.R;
import com.example.mobileapp.api.ApiService;
import com.example.mobileapp.api.RetrofitClient;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class AdminHomeAnalyticsFragment extends Fragment {

    private TextView tvAdminTodayRevenue, tvAdminNewOrdersCount, tvAdminProgressPercent;
    private ProgressBar progressAdminDailyTarget;
    private CardView btnAdminManageMenu, btnAdminManageOrders, btnAdminManagePromos, btnAdminManageReviews, btnAdminManageCustomers, btnAdminDetailedRevenue;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home_analytics, container, false);
        initViews(view);
        initEvents();
        loadAnalyticsData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Phân Tích Kinh Doanh");
        }
    }

    /**
     * Nguyễn Tuấn Kiệt
     * @param view
     */
    private void initViews(View view) {
        tvAdminTodayRevenue = view.findViewById(R.id.tvAdminTodayRevenue);
        tvAdminNewOrdersCount = view.findViewById(R.id.tvAdminNewOrdersCount);
        tvAdminProgressPercent = view.findViewById(R.id.tvAdminProgressPercent);
        progressAdminDailyTarget = view.findViewById(R.id.progressAdminDailyTarget);

        btnAdminManageMenu = view.findViewById(R.id.btnAdminManageMenu);
        btnAdminManageOrders = view.findViewById(R.id.btnAdminManageOrders);
        btnAdminManagePromos = view.findViewById(R.id.btnAdminManagePromos);
        btnAdminManageReviews = view.findViewById(R.id.btnAdminManageReviews);
        btnAdminManageCustomers = view.findViewById(R.id.btnAdminManageCustomers);
        btnAdminDetailedRevenue = view.findViewById(R.id.btnAdminDetailedRevenue);
    }

    /**
     * Nguyễn Tuấn Kiệt
     */
    private void initEvents() {
        btnAdminManageMenu.setOnClickListener(v -> navigateTo(new AdminMenuManagementFragment(), "Quản Lý Thực Đơn"));

        btnAdminManageOrders.setOnClickListener(v -> Toast.makeText(getContext(), "Mở Quản lý đơn hàng", Toast.LENGTH_SHORT).show());
        btnAdminManagePromos.setOnClickListener(v -> Toast.makeText(getContext(), "Mở Quản lý khuyến mãi", Toast.LENGTH_SHORT).show());
        btnAdminManageReviews.setOnClickListener(v -> Toast.makeText(getContext(), "Mở Quản lý đánh giá", Toast.LENGTH_SHORT).show());
        btnAdminManageCustomers.setOnClickListener(v -> Toast.makeText(getContext(), "Mở Quản lý khách hàng", Toast.LENGTH_SHORT).show());
        btnAdminDetailedRevenue.setOnClickListener(v -> Toast.makeText(getContext(), "Mở Thống kê chi tiết", Toast.LENGTH_SHORT).show());
    }

    /**
     * Nguyễn Tuấn Kiệt
     */
    private void loadAnalyticsData() {
        executorService.execute(() -> {
            try {
                ApiService apiService = RetrofitClient.getApiService();
                Response<Map<String, Object>> response = apiService.getAdminAnalytics().execute();

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();
                    final double revenue = (double) data.get("todayRevenue");
                    final double orders = (double) data.get("newOrders");
                    final int progressValue = ((Double) data.get("progress")).intValue();
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            tvAdminTodayRevenue.setText(String.format("%,.0fđ", revenue));
                            tvAdminNewOrdersCount.setText(String.format("%.0f Đơn", orders));
                            tvAdminProgressPercent.setText(progressValue + "%");
                            progressAdminDailyTarget.setProgress(progressValue);
                        });
                    }
                } else {
                    showErrorToast("Không thể tải dữ liệu phân tích");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorToast("Lỗi kết nối Server Spring Boot");
            }
        });
    }

    /**
     * Nguyễn Tuấn Kiệt
     * @param msg
     */
    private void showErrorToast(String msg) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * Nguyễn Tuấn Kiệt
     * @param fragment
     * @param toolbarTitle
     */
    private void navigateTo(Fragment fragment, String toolbarTitle) {
        if (getActivity() != null) {
            getParentFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.main_admin_container, fragment)
                    .addToBackStack(null)
                    .commit();

            if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(toolbarTitle);
            }
        }
    }

    /**
     * Nguyễn Tuấn Kiệt
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}