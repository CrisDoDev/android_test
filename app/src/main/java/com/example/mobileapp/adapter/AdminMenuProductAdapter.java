package com.example.mobileapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SwitchCompat;
import com.example.mobileapp.R;
import com.example.mobileapp.api.ApiService;
import com.example.mobileapp.api.RetrofitClient;
import com.example.mobileapp.model.Product;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class AdminMenuProductAdapter extends BaseAdapter {

    private final Context context;
    private final List<Product> productList;

    public AdminMenuProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() { return productList != null ? productList.size() : 0; }

    @Override
    public Object getItem(int position) { return productList.get(position); }

    @Override
    public long getItemId(int position) { return productList.get(position).getIdProduct(); }

    /**
     * Nguyễn Tuấn Kiệt
     * Dùng thêm sản phẩm (Quản lý Sản phẩm)
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_admin_menu_product, parent, false);
            holder = new ViewHolder();
            holder.imgProduct = convertView.findViewById(R.id.imgAdminProductItem);
            holder.tvName = convertView.findViewById(R.id.tvAdminProductNameItem);
            holder.tvPrice = convertView.findViewById(R.id.tvAdminProductPriceItem);
            holder.switchStatus = convertView.findViewById(R.id.switchAdminStatusItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = productList.get(position);
        holder.tvName.setText(product.getProductName());
        holder.tvPrice.setText(String.format("%,.0fđ", product.getBasePrice()));
        holder.switchStatus.setOnCheckedChangeListener(null);
        holder.switchStatus.setChecked(product.getStatus() == 1);
        holder.switchStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int newStatus = isChecked ? 1 : 0;
            product.setStatus(newStatus);
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    ApiService apiService = RetrofitClient.getApiService();
                    Response<Void> response = apiService.updateProductStatus((int) product.getIdProduct(), newStatus).execute();
                    if (!response.isSuccessful()) {
                        product.setStatus(isChecked ? 0 : 1);
                        updateUIOnMainThread(holder.switchStatus, !isChecked, "Cập nhật trạng thái thất bại");
                    } else {
                        String msg = isChecked ? "Đã bật kinh doanh: " : "Đã ngừng kinh doanh: ";
                        updateUIOnMainThread(null, false, msg + product.getProductName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    product.setStatus(isChecked ? 0 : 1);
                    updateUIOnMainThread(holder.switchStatus, !isChecked, "Mất kết nối Spring Boot");
                }
            });
        });

        return convertView;
    }

    /**
     * Nguyễn Tuấn Kiệt
     * Dùng thêm Sản phẩm (Quản lý Sản Phẩm)
     * @param switchView
     * @param rollbackState
     * @param toastMsg
     */
    private void updateUIOnMainThread(SwitchCompat switchView, boolean rollbackState, String toastMsg) {
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).runOnUiThread(() -> {
                if (switchView != null) {
                    switchView.setOnCheckedChangeListener(null);
                    switchView.setChecked(rollbackState);
                }
                Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();
            });
        }
    }

    static class ViewHolder {
        ImageView imgProduct;
        TextView tvName;
        TextView tvPrice;
        SwitchCompat switchStatus;
    }
}