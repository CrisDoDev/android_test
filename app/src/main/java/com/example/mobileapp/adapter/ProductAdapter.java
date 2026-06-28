package com.example.mobileapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mobileapp.R;
import com.example.mobileapp.fragment.ProductDetailFragment;
import com.example.mobileapp.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    /**
     * Nguyễn Tuấn Kiệt
     * Dùng trang chủ Khách Hàng
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    /**
     * Nguyễn Tuấn Kiệt
     * Dùng trang chủ Khách Hàng.
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvProductName.setText(product.getProductName());
        holder.tvDescription.setText(product.getDescription());
        holder.tvRating.setText(String.valueOf(product.getRatingStars()));

        // Xử lý giá tiền (Nếu có giá giảm finalPrice < basePrice)
        if (product.getFinalPrice() > 0 && product.getFinalPrice() < product.getBasePrice()) {
            holder.tvPrice.setText((int)(product.getFinalPrice() / 1000) + "k");
            holder.tvOriginalPrice.setText((int)(product.getBasePrice() / 1000) + "k");
            holder.tvOriginalPrice.setPaintFlags(holder.tvOriginalPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvOriginalPrice.setVisibility(View.VISIBLE);
        } else {
            holder.tvPrice.setText((int)(product.getBasePrice() / 1000) + "k");
            holder.tvOriginalPrice.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.imgProduct);

        holder.btnAddToCart.setOnClickListener(v -> {
            Toast.makeText(context, "Đã thêm " + product.getProductName() + " vào giỏ!", Toast.LENGTH_SHORT).show();
        });

        //thêm: Bấm vào item sản phẩm thì mở màn hình chi tiết
        holder.itemView.setOnClickListener(v -> {
            if (context instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, ProductDetailFragment.newInstance(product.getIdProduct()))
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvDescription, tvPrice, tvRating, tvOriginalPrice;
        FloatingActionButton btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}