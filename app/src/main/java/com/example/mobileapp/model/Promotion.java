package com.example.mobileapp.model;

import com.google.gson.annotations.SerializedName;

public class Promotion {
    private int idPromotion;
    private String couponCode;
    private double discountAmount;
    private double minOrderValue;
    private String expiryDate;
    private int status;
    private int isDeleted;

    public Promotion(){}
    public Promotion(int idPromotion, String couponCode, double discountAmount, double minOrderValue, String expiryDate, int status, int isDeleted) {
        this.idPromotion = idPromotion;
        this.couponCode = couponCode;
        this.discountAmount = discountAmount;
        this.minOrderValue = minOrderValue;
        this.expiryDate = expiryDate;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public int getIdPromotion() {
        return idPromotion;
    }

    public void setIdPromotion(int idPromotion) {
        this.idPromotion = idPromotion;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(double minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
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
}
