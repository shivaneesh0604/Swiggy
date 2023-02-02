package library;

import java.util.ArrayList;
import java.util.HashMap;
//todo: customer can acd orders only that are available in particular location
public final class Customer extends User {
    private final CustomerApplication customerApplication;
    private Location location;

    Customer(String userID, CustomerApplication application, Role role, String name) {
        super(userID, role, name);
        this.customerApplication = application;
    }

    public HashMap<Integer, Restaurant> getAllRestaurant(Location location){
        return customerApplication.getAllRestaurant(location);
    }

    public HashMap<String, Item> enterRestaurant(Restaurant restaurant, Timing timing) {
        return customerApplication.enterRestaurant(restaurant, timing);
    }

    public String addOrder(Item item, int quantity, Restaurant restaurant) {
        return customerApplication.takeOrder(item, quantity, this.getUserID(), restaurant);
    }

    public String removeOrder(Item item, int quantity, Restaurant restaurant) {
        return customerApplication.removeOrder(item, quantity, this.getUserID(), restaurant);
    }
    public HashMap<Integer, Order> viewItemsInCart() {
        return customerApplication.viewItemsInCart(this.getUserID());
    }

    public Bill confirmOrder(Restaurant restaurant) {
        return customerApplication.confirmOrder(this.getUserID(), restaurant);
    }

    public OrderStatus placeOrder(Restaurant restaurant) {
        if(this.location==null){
            return null;
        }
        return customerApplication.placeOrder(this.getUserID(), restaurant,this.location);
    }

    public ArrayList<Order> viewOrdersPlaced() {
        return customerApplication.viewOrdersPlaced(this.getUserID());
    }

    public OrderStatus cancelOrder(int orderID) {
        return customerApplication.cancelOrder(orderID);
    }

    public String checkStatusOfOrder(int orderID) {
        return customerApplication.checkStatusOfOrder(orderID);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
