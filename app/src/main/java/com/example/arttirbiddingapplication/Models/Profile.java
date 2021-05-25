package com.example.arttirbiddingapplication.Models;

public class Profile {

    private String name,surname,photoUrl,price;

    public Profile() {
    }

    public Profile(String name, String surname, String photoUrl, String price) {
        this.name = name;
        this.surname = surname;
        this.photoUrl = photoUrl;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
