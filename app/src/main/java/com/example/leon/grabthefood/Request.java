package com.example.leon.grabthefood;

/**
 * Created by leon on 4/18/17.
 */

public class Request {

    private String contact;
    private String deliveryAddress;
    private String food;
    private String price;
    private String remark;
    private String restaurantAddress;
    private String restaurantName;
    private String status;
    private String pickUpID;
    private String pickUpInfo;

    public String getContact() {
        return contact;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getFood() {
        return food;
    }

    public String getPrice() {
        return price;
    }

    public String getRemark() {
        return remark;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getStatus() {
        return status;
    }

    public String getPickUpID() {
        return pickUpID;
    }

    public String getPickUpInfo() {
        return pickUpInfo;
    }

    public Request() {
    }

    public String displayFood() {
        return "Food: " + food;
    }

    public String displayResInfo() {
        return "Restaurant info: " + restaurantName +
                "\n    " + restaurantAddress;
    }

    public String displayPrice() {
        return "Pay: $" + price;
    }

    public String displayRequesterInfo() {
        return "Contact info && Delivery: " + contact +
                "\n    " + deliveryAddress;
    }

    public String displayStatus() {
        return "Status: " + status;
    }

    public String displayRemark() {
        return "Remarks: " + remark;
    }

    public String displayPickUpInfo() {
        return "Provider Contact: " + pickUpInfo;
    }

    public Request(String contact, String deliveryAddress, String food, String price, String remark,
                   String restaurantAddress, String restaurantName, String status) {
        this.contact = contact;
        this.deliveryAddress = deliveryAddress;
        this.food = food;
        this.price = price;
        this.remark = remark;
        this.restaurantAddress = restaurantAddress;
        this.restaurantName = restaurantName;
        this.status = status;
        this.pickUpID = "";
        this.pickUpInfo = "";
    }
}
