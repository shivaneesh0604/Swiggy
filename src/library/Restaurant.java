package library;

import java.util.ArrayList;
import java.util.HashMap;

public class Restaurant {
    private final Location location;
    private final MenuItems menuItems;
    private final String restaurantName;
    private final int restaurantID;
    private final HashMap<Integer, ArrayList<LineOrder>> ordersGot;//orderID mapped with orders
    private final HashMap<Integer, ArrayList<LineOrder>> ordersCompleted;
    private RestaurantStatus restaurantStatus;

    Restaurant(Location location, String restaurantName, int restaurantID) {
        this.location = location;
        this.restaurantName = restaurantName;
        this.restaurantID = restaurantID;
        this.menuItems = new MenuItems();
        this.ordersGot = new HashMap<>();
        this.ordersCompleted = new HashMap<>();
        this.restaurantStatus = RestaurantStatus.AVAILABLE;
    }

    void receiveOrders(int orderID, ArrayList<LineOrder> lineOrders) {
        this.ordersGot.put(orderID, lineOrders);
    }

    HashMap<Integer, ArrayList<LineOrder>> viewOrderGot() {
        return new HashMap<>(ordersGot);
    }

    void setOrdersCompleted(int orderID) {
        ArrayList<LineOrder> orders = ordersGot.get(orderID);
        ordersCompleted.put(orderID, orders);
        ordersGot.remove(orderID);
    }

    void removeOrder(int orderID) {
        if (ordersGot.containsKey(orderID)) {
            this.ordersGot.remove(orderID);
        }
    }

    public Location getLocation() {
        return location;
    }

    MenuItems getMenuList() {
        return menuItems;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public RestaurantStatus getRestaurantStatus() {
        return restaurantStatus;
    }

    RestaurantStatus setRestaurantStatus(RestaurantStatus restaurantStatus) {
        this.restaurantStatus = restaurantStatus;
        return this.restaurantStatus;
    }
}
