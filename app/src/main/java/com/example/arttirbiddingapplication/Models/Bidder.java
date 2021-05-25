package com.example.arttirbiddingapplication.Models;

public class Bidder {

    private String bidderId,bidPrice;

    public Bidder(String bidderId, String bidPrice) {
        this.bidderId = bidderId;
        this.bidPrice = bidPrice;
    }

    public Bidder() {
    }

    public String getBidderId() {
        return bidderId;
    }

    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }
    @Override
    public String toString() {
        return "Bidder{" +
                "bidderId='" + bidderId + '\'' +
                ", bidPrice='" + bidPrice + '\'' +
                '}';
    }
}
