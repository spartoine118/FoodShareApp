package com.example.fromthestart;

import java.time.LocalDate;

public class FoodPost {

    private String location;
    private String foodttype;
    private int quantity;
    private String details;
    private String availabletill;
    private String createdate;
    private String postTitle;
    private String posterID;
    private String posterUsername;
    private String postID;

    public FoodPost(String postTitle, String location, String foodttype, int quantity, String details, String availabletill, String createdate, String posterId) {
        this.location = location;
        this.foodttype = foodttype;
        this.quantity = quantity;
        this.details = details;
        this.availabletill = availabletill;
        this.createdate = createdate;
        this.postTitle = postTitle;
        this.posterID = posterId;
    }

    public FoodPost(String postTitle, String location, String foodttype, int quantity, String details, String availabletill, String createdate, String posterId, String posterUsername) {
        this.location = location;
        this.foodttype = foodttype;
        this.quantity = quantity;
        this.details = details;
        this.availabletill = availabletill;
        this.createdate = createdate;
        this.postTitle = postTitle;
        this.posterID = posterId;
        this.posterUsername = posterUsername;
    }


    public FoodPost(){

    }

    public String getPosterUsername() {
        return posterUsername;
    }

    public void setPosterUsername(String posterUsername) {
        this.posterUsername = posterUsername;
    }

    public String getPosterID() {
        return posterID;
    }

    public void setPosterID(String posterID) {
        this.posterID = posterID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFoodttype() {
        return foodttype;
    }

    public void setFoodttype(String foodttype) {
        this.foodttype = foodttype;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAvailabletill() {
        return availabletill;
    }

    public void setAvailabletill(String availabletill) {
        this.availabletill = availabletill;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}
