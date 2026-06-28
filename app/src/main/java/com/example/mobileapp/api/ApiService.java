package com.example.mobileapp.api;

import com.example.mobileapp.model.Category;
import com.example.mobileapp.model.CheckResponse;
import com.example.mobileapp.model.Product;
import com.example.mobileapp.model.Review;
import com.example.mobileapp.model.Topping;
import com.example.mobileapp.model.User;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {

    // Check xem email này có ai đăng ký dưới MySQL chưa
    @GET("auth/check-email")
    Call<CheckResponse> checkEmail(@Query("email") String email);

    // Check xem số điện thoại này có ai dùng dưới MySQL chưa
    @GET("auth/check-phone")
    Call<CheckResponse> checkPhone(@Query("phone") String phone);

    // Đẩy thông tin user từ Firebase/Google xuống MySQL để lưu hoặc đăng nhập
    @POST("auth/sync-user")
    Call<User> syncUser(@Body User request);

    //bổ sung: Lấy sản phẩm theo mã danh mục
    @GET("products/category/{id}")
    Call<List<Product>> getProductsByCategory(@Path("id") int categoryId);

    //bổ sung: Lấy thông tin chi tiết của 1 sản phẩm
    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int productId);

    //bổ sung: Lấy danh sách bình luận của 1 sản phẩm
    @GET("reviews/product/{id}")
    Call<List<Review>> getReviewsByProduct(@Path("id") int productId);

    /**
     * Nguyễn Tuấn Kiệt
     * lấy sản phẩm bán chạy nhất.
     * Dùng trang chủ Khách Hàng
     * @return
     */
    @GET("products/bestsellers")
    Call<List<Product>> getBestSellerProducts();

    /**
     * Nguyễn Tuấn Kiệt
     * lấy danh mục đang có
     * Dùng trang chủ Khách hàng.
     * @return
     */
    @GET("categories")
    Call<List<Category>> getActiveCategories();

    /**
     * Nguyễn Tuấn Kiệt
     * lấy danh sách topping đang có
     * Dùng thêm Sản Phẩm (Quản lý Sản Phẩm)
     * @return
     */
    @GET("admin/toppings")
    Call<List<Topping>> getActiveToppings();

    /**
     * Nguyễn Tuấn Kiệt
     * Lấy chỉ số thống kê cơ bản.
     * Dùng trang chủ Admin.
     * @return
     */
    @GET("admin/dashboard/analytics")
    Call<Map<String, Object>> getAdminAnalytics();

    /**
     * Nguyễn Tuấn Kiệt
     * lấy danh sách sản phẩm cho Admin
     * dùng Quản lý Sản Phẩm
     * @return
     */
    @GET("admin/products")
    Call<List<Product>> getAdminAllProducts();

    /**
     * Nguyễn Tuấn Kiệt
     * cập nhật trạng thái sản phẩm
     * Dùng Quản lý Sản Phẩm.
     * @param productId
     * @param status
     * @return
     */
    @PUT("admin/products/{id}/status")
    Call<Void> updateProductStatus(@Path("id") int productId, @Query("status") int status);

    /**
     * Nguyễn Tuấn Kiệt
     * thêm nhiều sản phẩm một lần.
     * Dùng Thêm Sản Phẩm (Quản lý Sản Phẩm)
     * @param body
     * @return
     */
    @POST("admin/products/bulk")
    Call<ResponseBody> addBulkProducts(@Body Map<String, Object> body);
}