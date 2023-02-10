package library;

import java.util.ArrayList;
import java.util.HashMap;
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

    public OrderAddition addOrder(Item item, Restaurant restaurant) {
        return customerApplication.takeOrder(item, this.getUserID(), restaurant);
    }

    public OrderDeletion removeOrder(Item item, Restaurant restaurant) {
        return customerApplication.removeOrder(item, this.getUserID(), restaurant);
    }
    public HashMap<Integer, Order> viewItemsInCart() {
        return customerApplication.viewItemsInCart(this.getUserID());
    }

    public Bill confirmOrder(Restaurant restaurant) {
        return customerApplication.confirmOrder(this.getUserID(), restaurant);
    }

    public OrderStatus placeOrder(Restaurant restaurant) {
        return customerApplication.placeOrder(this.getUserID(), restaurant);
    }

    public ArrayList<Order> viewOrdersPlaced() {
        return customerApplication.viewOrdersPlaced(this.getUserID());
    }

    public OrderStatus cancelOrder(int orderID) {
        return customerApplication.cancelOrder(orderID);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
