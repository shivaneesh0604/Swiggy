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

    public void setLocation(String location) {
        this.location = location;
    }

    public HashMap<Integer, OrderList> viewItemsInCart(){
        return customerApplication.viewItemsInCart(this.getUserID());
    }
    public HashMap<String, Item> enterRestaurant(int restaurantID,Timing timing) {
        return customerApplication.enterRestaurant(restaurantID,timing);
    }

    public String addOrders(String foodName, int quantity, int restaurantID) {
        return customerApplication.TakeOrder(foodName, quantity, this.getUserID(), restaurantID);
    }

    public String deleteOrders(String foodName, int quantity, int restaurantID) {
        return customerApplication.deleteOrder(foodName, quantity, this.getUserID(), restaurantID);
    }
    public ArrayList<Order> viewOrder(int restaurantID){
        return customerApplication.viewOrder(restaurantID,this.getUserID());
    }
    public Bill getBill(int restaurantID){
        return customerApplication.getBill(this.getUserID(),restaurantID);
    }
    public Status confirmOrder(int restaurantID) {
        return customerApplication.confirmOrder(this.getUserID(), restaurantID);
    }

    public Status cancelOrder(int orderID){
        return customerApplication.cancelOrder(orderID);
    }

    public Status checkStatus(int orderID) {
        return customerApplication.checkStatus(orderID);
    }
    public String getLocation() {
        return location;
    }

}
