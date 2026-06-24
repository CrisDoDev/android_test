package com.example.mobileapp.model;

import com.google.gson.annotations.SerializedName;

public class User {
    private int idUser;
    private String firebaseUid;
    private int roleId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private int status;

    public User(){}
    public User(int idUser, String firebaseUid, int roleId, String fullName, String phoneNumber, String email, String avatarUrl, int status) {
        this.idUser = idUser;
        this.firebaseUid = firebaseUid;
        this.roleId = roleId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.status = status;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
