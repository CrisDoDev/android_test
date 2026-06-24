package com.example.mobileapp.model;

import com.google.gson.annotations.SerializedName;

public class Order {
    private int idOrder;
    private int userId;
    private Integer addressId;
    private Integer promotionId;
    private double totalProductPrice;
    private double shippingFee;
    private double discountPrice;
    private double finalTotal;
    private String paymentMethod;
    private String paymentStatus;
    private String orderStatus;
    private String shippingAddress;
    private String note;
    private String createdAt;

    public Order(){}
    public Order(int idOrder, int userId, Integer addressId, Integer promotionId, double totalProductPrice, double shippingFee, double discountPrice, double finalTotal, String paymentMethod, String paymentStatus, String orderStatus, String shippingAddress, String note, String createdAt) {
        this.idOrder = idOrder;
        this.userId = userId;
        this.addressId = addressId;
        this.promotionId = promotionId;
        this.totalProductPrice = totalProductPrice;
        this.shippingFee = shippingFee;
        this.discountPrice = discountPrice;
        this.finalTotal = finalTotal;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.orderStatus = orderStatus;
        this.shippingAddress = shippingAddress;
        this.note = note;
        this.createdAt = createdAt;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public double getTotalProductPrice() {
        return totalProductPrice;
    }

    public void setTotalProductPrice(double totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public double getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(double finalTotal) {
        this.finalTotal = finalTotal;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
