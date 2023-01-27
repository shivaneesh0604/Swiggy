package library;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends User {
    private final CustomerApplication customerApplication;
    private String location;

    public Customer(String userID, String passWord, Application application, Role role, String name) {
        super(userID, passWord, role, name);
        this.customerApplication = application;
    }

    public HashMap<String, Item> enterRestaurant(int restaurantID, Timing timing) {
        return customerApplication.enterRestaurant(restaurantID, timing);
    }

    public HashMap<Integer, OrderList> viewItemsInCart() {
        return customerApplication.viewItemsInCart(this.getUserID());
    }

    public String addOrders(String foodName, int quantity, int restaurantID) {
        return customerApplication.takeOrder(foodName.toUpperCase(), quantity, this.getUserID(), restaurantID);
    }

    public String deleteOrders(String foodName, int quantity, int restaurantID) {
        return customerApplication.deleteOrder(foodName.toUpperCase(), quantity, this.getUserID(), restaurantID);
    }

    public Bill confirmOrder(int restaurantID) {
        return customerApplication.confirmOrder(this.getUserID(), restaurantID);
    }

    public Status placeOrder(int restaurantID) {
        return customerApplication.placeOrder(this.getUserID(), restaurantID);
    }

    public ArrayList<OrderList> viewOrder() {
        return customerApplication.viewOrder(this.getUserID());
    }

    public Status cancelOrder(int orderID) {
        return customerApplication.cancelOrder(orderID);
    }

    public Status checkStatus(int orderID) {
        return customerApplication.checkStatus(orderID);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
