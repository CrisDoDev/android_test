package com.example.mobileapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetail {
    private int idOrderDetail;
    private int orderId;
    private int productId;
    private int quantity;
    private String selectedSize;
    private String selectedSugar;
    private String selectedIce;
    private double priceAtOrder;
    // Danh sách các Topping đi kèm
    private List<Topping> toppings;

    public OrderDetail(int idOrderDetail, int orderId, int quantity, int productId, String selectedSize, String selectedSugar, String selectedIce, double priceAtOrder) {
        this.idOrderDetail = idOrderDetail;
        this.orderId = orderId;
        this.quantity = quantity;
        this.productId = productId;
        this.selectedSize = selectedSize;
        this.selectedSugar = selectedSugar;
        this.selectedIce = selectedIce;
        this.priceAtOrder = priceAtOrder;
    }

    public int getIdOrderDetail() {
        return idOrderDetail;
    }

    public void setIdOrderDetail(int idOrderDetail) {
        this.idOrderDetail = idOrderDetail;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public double getPriceAtOrder() {
        return priceAtOrder;
    }

    public void setPriceAtOrder(double priceAtOrder) {
        this.priceAtOrder = priceAtOrder;
    }

    public List<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(List<Topping> toppings) {
        this.toppings = toppings;
    }
}
