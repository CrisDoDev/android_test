package com.example.mobileapp.model;

import com.google.gson.annotations.SerializedName;

public class Topping {
    private int idTopping;
    private String toppingName;
    private double price;
    private int status;
    private int isDeleted;
    // Biến phụ kiểm tra xem khách hàng có tích chọn Topping này ở màn hình chi tiết hay không
    private boolean isSelected = false;
    public Topping(){}
    public Topping(int idTopping, String toppingName, double price, int status, int isDeleted, boolean isSelected) {
        this.idTopping = idTopping;
        this.toppingName = toppingName;
        this.price = price;
        this.status = status;
        this.isDeleted = isDeleted;
        this.isSelected = isSelected;
    }

    public int getIdTopping() {
        return idTopping;
    }

    public void setIdTopping(int idTopping) {
        this.idTopping = idTopping;
    }

    public String getToppingName() {
        return toppingName;
    }

    public void setToppingName(String toppingName) {
        this.toppingName = toppingName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
