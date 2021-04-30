package com.example.arttirbiddingapplication;

import java.util.ArrayList;

public class Product {

    private String title,category,startingPrice,increasingRate,daysOfAuction,details,productId,sellerId;
    private ArrayList<String> pictures;
    private boolean condition;

    public Product(String productId,String category,String title,String startingPrice, String increasingRate, boolean condition, String daysOfAuction, String details, ArrayList<String> pictures,String sellerId) {
       this.sellerId=sellerId;
        this.productId=productId;
        this.title = title;
        this.category = category;
        this.startingPrice = startingPrice;
        this.increasingRate = increasingRate;
        this.condition = condition;
        this.daysOfAuction = daysOfAuction;
        this.details = details;
        this.pictures = pictures;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(String startingPrice) {
        this.startingPrice = startingPrice;
    }

    public String getIncreasingRate() {
        return increasingRate;
    }

    public void setIncreasingRate(String increasingRate) {
        this.increasingRate = increasingRate;
    }

    public boolean getCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public String getDaysOfAuction() {
        return daysOfAuction;
    }

    public void setDaysOfAuction(String daysOfAuction) {
        this.daysOfAuction = daysOfAuction;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }
}
