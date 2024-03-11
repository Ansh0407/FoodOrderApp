package com.android.foodorderapp;

public class Order {
    private String orderId;
    private String foodType;
    private String date;
    private String time;

    public Order(String orderId, String foodType, String date, String time) {
        this.orderId = orderId;
        this.foodType = foodType;
        this.date = date;
        this.time = time;
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
