package com.example.mobileapp.model;

import com.google.gson.annotations.SerializedName;

public class Category {
    private int idCategory;
    private String categoryName;
    private String imageUrl;
    private int isDeleted;
    public Category(){}
    public Category(int idCategory, String categoryName, String imageUrl, int isDeleted) {
        this.idCategory = idCategory;
        this.categoryName = categoryName;
        this.imageUrl = imageUrl;
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return this.categoryName;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
