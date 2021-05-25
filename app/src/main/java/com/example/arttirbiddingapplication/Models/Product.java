package com.example.arttirbiddingapplication.Models;

import java.util.ArrayList;
import java.util.Date;

public class Product {

    private String title,category,startingPrice,details,productId,sellerId,endDate;
    private ArrayList<String> pictures;
    private boolean condition;

    public Product(){

    }

    public Product(String productId, String category, String title,String startingPrice, boolean condition, String details,ArrayList<String> pictures, String sellerId,String endDate) {
        this.title = title;
        this.category = category;
        this.startingPrice = startingPrice;
        this.details = details;
        this.productId = productId;
        this.sellerId = sellerId;
        this.endDate = endDate;
        this.pictures = pictures;
        this.condition = condition;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public boolean isCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }
}
