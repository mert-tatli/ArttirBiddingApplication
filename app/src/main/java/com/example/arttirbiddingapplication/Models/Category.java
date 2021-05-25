package com.example.arttirbiddingapplication.Models;

public class Category {

    private String imageUrl;
    private String categoryName;

    public Category(String imageUrl, String categoryName) {
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
