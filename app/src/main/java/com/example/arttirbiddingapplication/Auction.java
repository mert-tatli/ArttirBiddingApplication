package com.example.arttirbiddingapplication;

import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Date;

public class Auction {
    private String currentprice,startingPrice,increasingRate,daysOfAuction,date;
    ArrayList<Bidder> bidders;

    public Auction() {
    }

    public Auction(String currentprice, String startingPrice, String increasingRate, String daysOfAuction, String date, ArrayList<Bidder> bidders) {
        this.currentprice = currentprice;
        this.startingPrice = startingPrice;
        this.increasingRate = increasingRate;
        this.daysOfAuction = daysOfAuction;
        this.date = date;
        this.bidders = bidders;
    }

    public String getCurrentprice() {
        return currentprice;
    }

    public void setCurrentprice(String currentprice) {
        this.currentprice = currentprice;
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

    public String getDaysOfAuction() {
        return daysOfAuction;
    }

    public void setDaysOfAuction(String daysOfAuction) {
        this.daysOfAuction = daysOfAuction;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Bidder> getBidders() {
        return bidders;
    }

    public void setBidders(ArrayList<Bidder> bidders) {
        this.bidders = bidders;
    }
}