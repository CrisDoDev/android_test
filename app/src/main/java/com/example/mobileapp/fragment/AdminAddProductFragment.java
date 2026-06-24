package com.example.mobileapp.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mobileapp.R;
import com.example.mobileapp.api.ApiService;
import com.example.mobileapp.api.RetrofitClient;
import com.example.mobileapp.model.Category;
import com.example.mobileapp.model.Product;
import com.example.mobileapp.model.Topping;

import okhttp3.ResponseBody;
import java.io.IOException;
import java.util.*;

public class AdminAddProductFragment extends Fragment {

    private Spinner spnAdminCategory;
    private LinearLayout layoutAdminToppingContainer, layoutAdminDraftContainer;
    private EditText edtAdminProductName, edtAdminProductPrice;
    private Button btnAdminSelectImage, btnAdminAddToDraft, btnAdminSaveAll;
    private ImageView imgAdminProductPreview;
    private TextView tvAdminDraftTitle;

    private final List<Product> draftProductList = new ArrayList<>();
    private final List<Integer> selectedToppingIds = new ArrayList<>();
    private int selectedCategoryId = 1;

    // Khai báo các Launcher chuẩn mới để thay thế startActivityForResult
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    /**
     * Nguyễn Tuấn Kiệt
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_add_product, container, false);
        initViews(view);
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        if (extras != null) {
                            Bitmap bitmap = (Bitmap) extras.get("data");
                            imgAdminProductPreview.setImageBitmap(bitmap);
                        }
                    }
                }
        );
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        imgAdminProductPreview.setImageURI(selectedImage);
                    }
                }
        );

        loadDataFromSpringBoot();
        initEvents();
        return view;
    }

    /**
     * Nguyễn Tuấn Kiệt
     * @param view
     */
    private void initViews(View view) {
        spnAdminCategory = view.findViewById(R.id.spnAdminCategory);
        layoutAdminToppingContainer = view.findViewById(R.id.layoutAdminToppingContainer);
        layoutAdminDraftContainer = view.findViewById(R.id.layoutAdminDraftContainer);
        edtAdminProductName = view.findViewById(R.id.edtAdminProductName);
        edtAdminProductPrice = view.findViewById(R.id.edtAdminProductPrice);
        btnAdminSelectImage = view.findViewById(R.id.btnAdminSelectImage);
        btnAdminAddToDraft = view.findViewById(R.id.btnAdminAddToDraft);
        btnAdminSaveAll = view.findViewById(R.id.btnAdminSaveAll);
        imgAdminProductPreview = view.findViewById(R.id.imgAdminProductPreview);
        tvAdminDraftTitle = view.findViewById(R.id.tvAdminDraftTitle);
    }

    /**
     * Nguyễn Tuấn Kiệt
     */
    private void initEvents() {
        btnAdminSelectImage.setOnClickListener(v -> showImageSourceDialog());
        btnAdminAddToDraft.setOnClickListener(v -> addCurrentProductToDraft());
        btnAdminSaveAll.setOnClickListener(v -> saveAllProductsToServer());
    }

    /**
     * Nguyễn Tuấn Kiệt
     */
    private void showImageSourceDialog() {
        CharSequence[] options = {"Chụp từ máy ảnh (Camera)", "Chọn từ thiết bị (Gallery)", "Hủy"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Phương thức thêm ảnh sản phẩm");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Chụp từ máy ảnh (Camera)")) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(takePicture);
            } else if (options[item].equals("Chọn từ thiết bị (Gallery)")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryLauncher.launch(pickPhoto);
            } else {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * Nguyễn Tuấn Kiệt
     */
    private void addCurrentProductToDraft() {
        String name = edtAdminProductName.getText().toString().trim();
        String priceStr = edtAdminProductPrice.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập tên và giá nước uống", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        Product product = new Product();
        product.setProductName(name);
        product.setBasePrice(price);
        draftProductList.add(product);
        edtAdminProductName.setText("");
        edtAdminProductPrice.setText("");
        imgAdminProductPreview.setImageResource(android.R.drawable.ic_menu_gallery);

        updateDraftUI();
    }

    /**
     * Nguyễn Tuấn Kiệt
     */
    private void updateDraftUI() {
        layoutAdminDraftContainer.removeAllViews();
        tvAdminDraftTitle.setText("Danh sách chờ lưu (" + draftProductList.size() + " món)");

        for (int i = 0; i < draftProductList.size(); i++) {
            Product p = draftProductList.get(i);
            TextView tvItem = new TextView(getContext());
            tvItem.setText((i + 1) + ". " + p.getProductName() + " - " + String.format("%,.0fđ", p.getBasePrice()));
            tvItem.setPadding(10, 10, 10, 10);
            tvItem.setTextSize(14f);
            layoutAdminDraftContainer.addView(tvItem);
        }
        btnAdminSaveAll.setVisibility(draftProductList.isEmpty() ? View.GONE : View.VISIBLE);
    }

    /**
     * Nguyễn Tuấn Kiệt
     */
    private void saveAllProductsToServer() {
        selectedToppingIds.clear();
        for (int i = 0; i < layoutAdminToppingContainer.getChildCount(); i++) {
            CheckBox cb = (CheckBox) layoutAdminToppingContainer.getChildAt(i);
            if (cb.isChecked()) {
                selectedToppingIds.add((Integer) cb.getTag());
            }
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("categoryId", selectedCategoryId);
        payload.put("toppingIds", selectedToppingIds);
        payload.put("products", draftProductList);

        RetrofitClient.getApiService().addBulkProducts(payload).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đã lưu thành công tất cả món ăn!", Toast.LENGTH_LONG).show();
                    draftProductList.clear();
                    getParentFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Có lỗi xảy ra từ máy chủ Spring Boot", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối mạng hoàn toàn!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Nguyễn Tuấn Kiệt
     */
    private void loadDataFromSpringBoot() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getActiveCategories().enqueue(new retrofit2.Callback<List<Category>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Category>> call, retrofit2.Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categoryList = response.body();
                    ArrayAdapter<Category> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnAdminCategory.setAdapter(adapter);
                    spnAdminCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Category selectedCategory = (Category) parent.getItemAtPosition(position);
                            selectedCategoryId = (int) selectedCategory.getIdCategory();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                } else {
                    Toast.makeText(getContext(), "Không thể tải danh sách danh mục!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<List<Category>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng khi tải danh mục!", Toast.LENGTH_SHORT).show();
            }
        });

        apiService.getActiveToppings().enqueue(new retrofit2.Callback<List<Topping>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Topping>> call, retrofit2.Response<List<Topping>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Topping> toppingList = response.body();
                    layoutAdminToppingContainer.removeAllViews();
                    for (Topping topping : toppingList) {
                        CheckBox cb = new CheckBox(getContext());
                        cb.setText(topping.getToppingName() + " (+" + String.format("%,.0fđ", topping.getPrice()) + ")");
                        cb.setTag((int) topping.getIdTopping());
                        layoutAdminToppingContainer.addView(cb);
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể tải danh sách Topping!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<List<Topping>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng khi tải Topping!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}