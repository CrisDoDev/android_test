package com.example.mobileapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "local_cart")
public class CartItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int productId;
    private String productName;
    private double basePrice;
    private String imageUrl;
    private int quantity;
    private String selectedSize;
    private String selectedSugar;
    private String selectedIce;

//      Lưu danh sách ID các Topping đi kèm. Ví dụ: "[1, 4]" ứng với Trân châu đen và Thạch phô mai
//      Ta không dùng cấu trúc List là vì Room (SQLite) chỉ hỗ trợ kiểu dữ liệu nguyên thủy, dùng List sẽ không biết ép kiểu sẽ lỗi biên dịch
    private String toppingIdsJson;
    private String toppingNamesDisplay; // Ví dụ: "Trân châu đen, Thạch phô mai" để hiện lên UI luôn
    private double totalToppingsPrice;

    public CartItem(int id, int productId, String productName, double basePrice, String imageUrl, int quantity, String selectedSize, String selectedSugar, String selectedIce, String toppingIdsJson, String toppingNamesDisplay, double totalToppingsPrice) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.basePrice = basePrice;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.selectedSize = selectedSize;
        this.selectedSugar = selectedSugar;
        this.selectedIce = selectedIce;
        this.toppingIdsJson = toppingIdsJson;
        this.toppingNamesDisplay = toppingNamesDisplay;
        this.totalToppingsPrice = totalToppingsPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(String selectedSize) {
        this.selectedSize = selectedSize;
    }

    public String getSelectedSugar() {
        return selectedSugar;
    }

    public void setSelectedSugar(String selectedSugar) {
        this.selectedSugar = selectedSugar;
    }

    public String getSelectedIce() {
        return selectedIce;
    }

    public void setSelectedIce(String selectedIce) {
        this.selectedIce = selectedIce;
    }

    public String getToppingIdsJson() {
        return toppingIdsJson;
    }

    public void setToppingIdsJson(String toppingIdsJson) {
        this.toppingIdsJson = toppingIdsJson;
    }

    public String getToppingNamesDisplay() {
        return toppingNamesDisplay;
    }

    public void setToppingNamesDisplay(String toppingNamesDisplay) {
        this.toppingNamesDisplay = toppingNamesDisplay;
    }

    public double getTotalToppingsPrice() {
        return totalToppingsPrice;
    }

    public void setTotalToppingsPrice(double totalToppingsPrice) {
        this.totalToppingsPrice = totalToppingsPrice;
    }
}
