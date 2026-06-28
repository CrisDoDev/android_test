package com.example.mobileapp.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mobileapp.R;
import com.example.mobileapp.adapter.ReviewAdapter;
import com.example.mobileapp.api.RetrofitClient;
import com.example.mobileapp.model.Product;
import com.example.mobileapp.model.Review;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class ProductDetailFragment extends Fragment {

    private int productId;
    private ImageView imgProduct;
    private TextView txtName, txtPromoPrice, txtOriginalPrice, txtDesc, txtStars;
    private RecyclerView rvReviews;
    private ReviewAdapter reviewAdapter;
    private final List<Review> reviewList = new ArrayList<>();

    // Ánh xạ thêm nút bấm
    private FloatingActionButton btnAddToCart;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static ProductDetailFragment newInstance(int prodId) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putInt("PRODUCT_ID", prodId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getInt("PRODUCT_ID");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        imgProduct = view.findViewById(R.id.imgProductDetail);
        txtName = view.findViewById(R.id.txtDetailProductName);
        txtPromoPrice = view.findViewById(R.id.txtDetailPromoPrice);
        txtOriginalPrice = view.findViewById(R.id.txtDetailOriginalPrice);
        txtDesc = view.findViewById(R.id.txtProductDescription);
        txtStars = view.findViewById(R.id.txtDetailRatingStars);
        rvReviews = view.findViewById(R.id.rvProductReviews);

        // Ánh xạ nút mới thêm vào
        btnAddToCart = view.findViewById(R.id.btnAddToBagDetail);

        view.findViewById(R.id.btnBackFromDetail).setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        rvReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewAdapter = new ReviewAdapter(getContext(), reviewList);
        rvReviews.setAdapter(reviewAdapter);

        // Kích hoạt sự kiện bấm nút cộng
        btnAddToCart.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Kích hoạt bảng chọn Size & Topping !", Toast.LENGTH_LONG).show();
        });

        loadData();
        return view;
    }

    private void loadData() {
        executorService.execute(() -> {
            try {
                Response<Product> prodResponse = RetrofitClient.getApiService().getProductById(productId).execute();
                Response<List<Review>> reviewResponse = RetrofitClient.getApiService().getReviewsByProduct(productId).execute();

                if (prodResponse.isSuccessful() && prodResponse.body() != null) {
                    Product product = prodResponse.body();
                    mainHandler.post(() -> {
                        txtName.setText(product.getProductName());
                        txtDesc.setText(product.getDescription());
                        txtStars.setText(product.getRatingStars() + "/5");

                        if (product.getFinalPrice() > 0 && product.getFinalPrice() < product.getBasePrice()) {
                            txtPromoPrice.setText(String.format("%,d", (int)product.getFinalPrice()) + "đ");
                            txtOriginalPrice.setText(String.format("%,d", (int)product.getBasePrice()) + "đ");
                            txtOriginalPrice.setPaintFlags(txtOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            txtOriginalPrice.setVisibility(View.VISIBLE);
                        } else {
                            txtPromoPrice.setText(String.format("%,d", (int)product.getBasePrice()) + "đ");
                            txtOriginalPrice.setVisibility(View.GONE);
                        }
                        Glide.with(this).load(product.getImageUrl()).into(imgProduct);
                    });
                }

                if (reviewResponse.isSuccessful() && reviewResponse.body() != null) {
                    mainHandler.post(() -> {
                        reviewList.clear();
                        reviewList.addAll(reviewResponse.body());
                        reviewAdapter.notifyDataSetChanged();
                    });
                }
            } catch (Exception e) {
                mainHandler.post(() -> Toast.makeText(getContext(), "Lỗi kết nối Server!", Toast.LENGTH_SHORT).show());
            }
        });
    }
}