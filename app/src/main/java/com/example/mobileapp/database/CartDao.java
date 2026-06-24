package com.example.mobileapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface CartDao {

    // Thêm một ly trà sữa mới vào giỏ hàng local
    @Insert
    void insertToCart(CartItem item);

    // Lấy ra toàn bộ danh sách món đang có trong giỏ hàng để hiện lên màn hình Giỏ Hàng
    @Query("SELECT * FROM local_cart")
    List<CartItem> getAllCartItems();

    // Cập nhật số lượng khi khách bấm nút tăng/giảm (+/-) số lượng trên giao diện
    @Update
    void updateCartItem(CartItem item);

    // Xóa một món ra khỏi giỏ hàng khi họ không muốn mua ly này nữa
    @Delete
    void deleteCartItem(CartItem item);

    // Xóa sạch giỏ hàng sau khi đặt hàng thành công
    @Query("DELETE FROM local_cart")
    void clearCart();
}