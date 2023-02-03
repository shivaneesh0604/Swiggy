package library;

import java.util.ArrayList;
import java.util.HashMap;

public class Restaurant {
    private final Location location;
    private final MenuItems menuItems;
    private final String restaurantName;
    private final int restaurantID;
    private final HashMap<Integer, ArrayList<LineOrder>> ordersGot;//orderID mapped with orders
    private HashMap<Integer,ArrayList<LineOrder>> ordersCompleted;
    private RestaurantStatus restaurantStatus;

    Restaurant(Location location, String restaurantName, int restaurantID) {
        this.location = location;
        this.restaurantName = restaurantName;
        this.restaurantID = restaurantID;
        this.menuItems = new MenuItems();
        this.ordersGot = new HashMap<>();
        this.ordersCompleted = new HashMap<>();
        this.restaurantStatus= RestaurantStatus.AVAILABLE;
    }

    void  receiveOrders(int orderID, ArrayList<LineOrder> lineOrders) {
        this.ordersGot.put(orderID, lineOrders);
    }

    HashMap<Integer, ArrayList<LineOrder>> viewOrderGot() {
        return ordersGot;
    }

    public Location getLocation() {
        return location;
    }

    public MenuItems getMenuList() {
        return menuItems;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    RestaurantStatus getRestaurantStatus() {
        return restaurantStatus;
    }

    RestaurantStatus setRestaurantStatus(RestaurantStatus restaurantStatus) {
        this.restaurantStatus = restaurantStatus;
        return restaurantStatus;
    }
}
