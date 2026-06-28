package com.example.mobileapp.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    private int idProduct;
    private int categoryId;
    private String productName;
    private double basePrice;
    private double finalPrice;      // BỔ SUNG: Giá cuối cùng sau khi giảm
    private String imageUrl;
    private String description;
    private int status;
    private int isDeleted;
    // Thuộc tính bổ trợ lấy điểm đánh giá trung bình từ API trả về để hiển thị UI Trang chủ
    private double ratingStars = 5.0;

    public Product(){}
    public Product(int idProduct, int categoryId, String productName, double basePrice, String imageUrl, String description, int status, int isDeleted, double ratingStars) {
        this.idProduct = idProduct;
        this.categoryId = categoryId;
        this.productName = productName;
        this.basePrice = basePrice;
        this.imageUrl = imageUrl;
        this.description = description;
        this.status = status;
        this.isDeleted = isDeleted;
        this.ratingStars = ratingStars;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public double getRatingStars() {
        return ratingStars;
    }

    public void setRatingStars(double ratingStars) {
        this.ratingStars = ratingStars;
    }

    public double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(double finalPrice) { this.finalPrice = finalPrice; }
}

