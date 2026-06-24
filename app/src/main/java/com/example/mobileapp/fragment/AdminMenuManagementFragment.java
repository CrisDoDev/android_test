package com.example.mobileapp.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mobileapp.R;
import com.example.mobileapp.adapter.AdminMenuProductAdapter;
import com.example.mobileapp.api.ApiService;
import com.example.mobileapp.api.RetrofitClient;
import com.example.mobileapp.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class AdminMenuManagementFragment extends Fragment {
    private EditText edtAdminSearchMenu;
    private ProgressBar progressAdminMenuLoading;
    private ListView lvAdminMenuProducts;
    private FloatingActionButton fabAdminAddProduct;
    private List<Product> originalList;
    private List<Product> filteredList;
    private AdminMenuProductAdapter adapter;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_menu_management, container, false);
        initViews(view);
        initEvents();
        fetchProductMenu();
        return view;
    }

    /**
     * Nguyễn Tuấn Kiệt
     * @param view
     */
    private void initViews(View view) {
        edtAdminSearchMenu = view.findViewById(R.id.edtAdminSearchMenu);
        progressAdminMenuLoading = view.findViewById(R.id.progressAdminMenuLoading);
        lvAdminMenuProducts = view.findViewById(R.id.lvAdminMenuProducts);
        fabAdminAddProduct = view.findViewById(R.id.fabAdminAddProduct);
        originalList = new ArrayList<>();
        filteredList = new ArrayList<>();
    }

    /**
     * Nguyễn Tuấn Kiệt
     */
    private void initEvents() {
        edtAdminSearchMenu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMenu(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        fabAdminAddProduct.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.main_admin_container, new AdminAddProductFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }

    /**
     * Nguyễn Tuấn Kiệt
     */
    private void fetchProductMenu() {
        progressAdminMenuLoading.setVisibility(View.VISIBLE);
        executorService.execute(() -> {
            try {
                ApiService apiService = RetrofitClient.getApiService();
                Response<List<Product>> response = apiService.getAdminAllProducts().execute();

                if (response.isSuccessful() && response.body() != null) {
                    originalList.clear();
                    originalList.addAll(response.body());

                    filteredList.clear();
                    filteredList.addAll(originalList);

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            progressAdminMenuLoading.setVisibility(View.GONE);
                            adapter = new AdminMenuProductAdapter(getContext(), filteredList);
                            lvAdminMenuProducts.setAdapter(adapter);
                        });
                    }
                } else {
                    showError("Lỗi đọc danh sách thực đơn");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError("Lỗi kết nối cơ sở dữ liệu");
            }
        });
    }

    /**
     * Nguyễn Tuấn Kiệt
     * @param msg
     */
    private void showError(String msg) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                progressAdminMenuLoading.setVisibility(View.GONE);
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            });
        }
    }

    /**
     * Nguyễn Tuấn Kiệt
     * @param query
     */
    private void filterMenu(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            for (Product p : originalList) {
                if (p.getProductName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(p);
                }
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}