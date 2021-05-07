package com.example.arttirbiddingapplication;

public class BidderList {

    private String bidderId,bidderName,bidPrice,userProfile;

    public BidderList(String bidderId, String bidderName, String bidPrice, String userProfile) {
        this.bidderId = bidderId;
        this.bidderName = bidderName;
        this.bidPrice = bidPrice;
        this.userProfile = userProfile;
    }

    public BidderList() {
    }

    public String getBidderId() {
        return bidderId;
    }

    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }
}
