package com.android.foodorderapp;

public class Order {
    private int id;
    private String customerName;
    private String deliveryCity;
    private String restaurantName;
    private String items;
    private float totalAmount;
    private String foodType;
    private String date;
    private String time;

    public Order() {
        // Default constructor
    }

    public Order(int id, String customerName, String deliveryCity, String restaurantName, String items, float totalAmount, String foodType, String date, String time) {
        this.id = id;
        this.customerName = customerName;
        this.deliveryCity = deliveryCity;
        this.restaurantName = restaurantName;
        this.items = items;
        this.totalAmount = totalAmount;
        this.foodType = foodType;
        this.date = date;
        this.time = time;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
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
