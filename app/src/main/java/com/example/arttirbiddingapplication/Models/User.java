package com.example.arttirbiddingapplication.Models;

public class User {
    private String userId,userName,userSurname,profileImage,email;

    public User() {
    }
    public User(String userId, String userName, String userSurname, String profileImage,String email) {
        this.userId = userId;
        this.userName = userName;
        this.userSurname = userSurname;
        this.profileImage = profileImage;
        this.email=email;
    }
    public User(String userId, String userName, String userSurname, String profileImage) {
        this.userId = userId;
        this.userName = userName;
        this.userSurname = userSurname;
        this.profileImage = profileImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
