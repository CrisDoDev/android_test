package com.example.mobileapp.model;

import com.google.gson.annotations.SerializedName;

public class Address {
    private int idAddress;
    private int userId;
    private String addressLabel;
    private String receiverName;
    private String receiverPhone;
    private String fullAddressText;
    private double latitude;
    private double longitude;
    private int isDefault;
    private int isDeleted;
    public Address(){}
    public Address(int idAddress, int userId, String addressLabel, String receiverName, String receiverPhone, String fullAddressText, double latitude, double longitude, int isDefault, int isDeleted) {
        this.idAddress = idAddress;
        this.userId = userId;
        this.addressLabel = addressLabel;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.fullAddressText = fullAddressText;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isDefault = isDefault;
        this.isDeleted = isDeleted;
    }

    public int getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(int idAddress) {
        this.idAddress = idAddress;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddressLabel() {
        return addressLabel;
    }

    public void setAddressLabel(String addressLabel) {
        this.addressLabel = addressLabel;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getFullAddressText() {
        return fullAddressText;
    }

    public void setFullAddressText(String fullAddressText) {
        this.fullAddressText = fullAddressText;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
