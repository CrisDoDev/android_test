package com.example.mobileapp.model;

import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName(value = "idReview", alternate = {"id_review"})
    private int idReview;
    @SerializedName(value = "userId", alternate = {"user_id"})
    private int userId;

    @SerializedName("userName")
    private String userName;
    @SerializedName(value = "productId", alternate = {"product_id"})
    private int productId;
    @SerializedName(value = "orderId", alternate = {"order_id"})
    private int orderId;
    @SerializedName(value = "ratingStars", alternate = {"rating_stars"})
    private int ratingStars;
    private String comment;
    @SerializedName(value = "adminReply", alternate = {"admin_reply"})
    private String adminReply;
    private int status;
    @SerializedName(value = "createdAt", alternate = {"created_at"})
    private String createdAt;

    public Review(){}
    public Review(int idReview, int userId, int productId, int ratingStars, int orderId, String comment, String adminReply, int status, String createdAt) {
        this.idReview = idReview;
        this.userId = userId;
        this.productId = productId;
        this.ratingStars = ratingStars;
        this.orderId = orderId;
        this.comment = comment;
        this.adminReply = adminReply;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getIdReview() {
        return idReview;
    }

    public void setIdReview(int idReview) {
        this.idReview = idReview;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getRatingStars() {
        return ratingStars;
    }

    public void setRatingStars(int ratingStars) {
        this.ratingStars = ratingStars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAdminReply() {
        return adminReply;
    }

    public void setAdminReply(String adminReply) {
        this.adminReply = adminReply;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
