package com.example.mobileapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobileapp.R;
import com.example.mobileapp.adapter.ProductAdapter;
import com.example.mobileapp.api.RetrofitClient;
import com.example.mobileapp.model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class CategoryProductsFragment extends Fragment {

    private int categoryId;
    private String categoryName;
    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private SeekBar priceSeekBar;
    private TextView txtCurrentPrice;

    private final List<Product> allProductsList = new ArrayList<>();
    private final List<Product> filteredList = new ArrayList<>();

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static CategoryProductsFragment newInstance(int catId, String catName) {
        CategoryProductsFragment fragment = new CategoryProductsFragment();
        Bundle args = new Bundle();
        args.putInt("CATEGORY_ID", catId);
        args.putString("CATEGORY_NAME", catName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt("CATEGORY_ID");
            categoryName = getArguments().getString("CATEGORY_NAME");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_products, container, false);

        TextView txtTitle = view.findViewById(R.id.txtCategoryTitle);
        txtTitle.setText(categoryName);

        rvProducts = view.findViewById(R.id.rvCategoryProducts);
        priceSeekBar = view.findViewById(R.id.priceSeekBar);
        txtCurrentPrice = view.findViewById(R.id.txtCurrentFilterPrice);

        view.findViewById(R.id.btnBack).setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productAdapter = new ProductAdapter(getContext(), filteredList);
        rvProducts.setAdapter(productAdapter);

        priceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtCurrentPrice.setText("Dưới " + String.format("%,d", progress) + "đ");
                filteredList.clear();
                for (Product p : allProductsList) {
                    double actualPrice = p.getFinalPrice() > 0 ? p.getFinalPrice() : p.getBasePrice();
                    if (actualPrice <= progress) {
                        filteredList.add(p);
                    }
                }
                productAdapter.notifyDataSetChanged();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        loadProducts();
        return view;
    }

    private void loadProducts() {
        executorService.execute(() -> {
            try {
                Response<List<Product>> response = RetrofitClient.getApiService().getProductsByCategory(categoryId).execute();
                if (response.isSuccessful() && response.body() != null) {
                    allProductsList.clear();
                    allProductsList.addAll(response.body());
                    mainHandler.post(() -> {
                        filteredList.clear();
                        filteredList.addAll(allProductsList);
                        productAdapter.notifyDataSetChanged();
                    });
                }
            } catch (Exception e) {
                mainHandler.post(() -> Toast.makeText(getContext(), "Lỗi kết nối Server!", Toast.LENGTH_SHORT).show());
            }
        });
    }
}