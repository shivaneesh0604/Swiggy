package library;

import java.util.ArrayList;
import java.util.HashMap;

public final class Customer extends User {
    private final CustomerApplication customerApplication;
    private String location;

    public Customer(String userID, CustomerApplication application, Role role, String name) {
        super(userID, role, name);
        this.customerApplication = application;
    }

    public HashMap<Integer, String> getAllRestaurant(){
        return customerApplication.getAllRestaurant();
    }

    public HashMap<String, Item> enterRestaurant(int restaurantID, Timing timing) {
        return customerApplication.enterRestaurant(restaurantID, timing);
    }

    public String addOrder(String foodName, int quantity, int restaurantID) {
        return customerApplication.takeOrder(foodName.toUpperCase(), quantity, this.getUserID(), restaurantID);
    }

    public String deleteOrder(String foodName, int quantity, int restaurantID) {
        return customerApplication.deleteOrder(foodName.toUpperCase(), quantity, this.getUserID(), restaurantID);
    }
    public HashMap<Integer, OrderList> viewItemsInCart() {
        return customerApplication.viewItemsInCart(this.getUserID());
    }

    public Bill confirmOrder(int restaurantID) {
        return customerApplication.confirmOrder(this.getUserID(), restaurantID);
    }

    public Status placeOrder(int restaurantID) {
        if(this.location==null){
            return null;
        }
        return customerApplication.placeOrder(this.getUserID(), restaurantID);
    }

    public ArrayList<OrderList> viewOrdersPlaced() {
        return customerApplication.viewOrdersPlaced(this.getUserID());
    }

    public Status cancelOrder(int orderID) {
        return customerApplication.cancelOrder(orderID);
    }

    public String checkStatusOfOrder(int orderID) {
        return customerApplication.checkStatusOfOrder(orderID);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
