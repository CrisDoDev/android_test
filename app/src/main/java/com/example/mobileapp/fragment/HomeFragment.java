package com.example.mobileapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobileapp.R;
import com.example.mobileapp.adapter.CategoryAdapter;
import com.example.mobileapp.adapter.ProductAdapter;
import com.example.mobileapp.model.Category;
import com.example.mobileapp.model.Product;
import com.example.mobileapp.api.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView rvBestSellers;
    private RecyclerView rvCategories;
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;
    private List<Product> realProductList;
    private List<Category> realCategoryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvBestSellers = view.findViewById(R.id.rvBestSellers);
        rvCategories = view.findViewById(R.id.rvCategories);

        realProductList = new ArrayList<>();
        realCategoryList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvBestSellers.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(getContext(), realProductList);
        rvBestSellers.setAdapter(productAdapter);

        LinearLayoutManager layoutManagerCategory = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCategories.setLayoutManager(layoutManagerCategory);
        categoryAdapter = new CategoryAdapter(getContext(), realCategoryList);
        rvCategories.setAdapter(categoryAdapter);

        fetchProductsFromDatabase();
        fetchCategoriesFromDatabase();

        view.findViewById(R.id.imgHomeAccount).setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new com.example.mobileapp.fragment.AccountFragment())
                    .addToBackStack(null) // Bấm nút Back trên điện thoại sẽ quay về Trang Chủ
                    .commit();
        });

        return view;
    }

    /**
     * Nguyễn Tuấn Kiệt
     */
    private void fetchCategoriesFromDatabase() {
        RetrofitClient.getApiService().getActiveCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (!isAdded() || getContext() == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    realCategoryList.clear();
                    realCategoryList.addAll(response.body());
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Lỗi tải danh mục: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                if (isAdded() && getContext() != null) {
                    Toast.makeText(requireContext(), "Lỗi kết nối danh mục: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Nguyễn Tuấn Kiệt
     */
    private void fetchProductsFromDatabase() {
        RetrofitClient.getApiService().getBestSellerProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (!isAdded() || getContext() == null) return;
                if (response.isSuccessful() && response.body() != null) {
                    realProductList.clear();
                    realProductList.addAll(response.body());
                    if (realProductList.isEmpty()) {
                        Toast.makeText(requireContext(), "Kết nối OK nhưng Database chưa có sản phẩm nào phù hợp!", Toast.LENGTH_LONG).show();
                    } else {
                        productAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    String errorMsg = "Lỗi Server: " + response.code();
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                if (!isAdded() || getContext() == null) return;
                String chiTietLoi = t.getMessage();
                Toast.makeText(requireContext(), "Lỗi kết nối/ép kiểu: " + chiTietLoi, Toast.LENGTH_LONG).show();
            }
        });
    }
}