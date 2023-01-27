package library;

import java.util.ArrayList;
import java.util.HashMap;

public class Restaurant {
    private final String location;
    private final MenuList menuList;
    private final String restaurantName;
    private final int restaurantID;
    private final HashMap<Integer, ArrayList<Order>> ordersGot;//orderID mapped with orders
    private HashMap<Integer,ArrayList<Order>> ordersCompleted;

    public Restaurant(String location, String restaurantName, int restaurantID) {
        this.location = location;
        this.restaurantName = restaurantName;
        this.restaurantID = restaurantID;
        this.menuList = new MenuList();
        this.ordersGot = new HashMap<>();
        this.ordersCompleted = new HashMap<>();
    }

    void receiveOrders(int orderID, ArrayList<Order> orders) {
        this.ordersGot.put(orderID, orders);
    }

    void completeOrder(int orderID){
        ArrayList<Order> order = this.ordersGot.get(orderID);
        ordersCompleted.put(orderID,order);
        ordersGot.remove(orderID);
    }
    HashMap<Integer, ArrayList<Order>> viewOrder() {
        return ordersGot;
    }

    public String getLocation() {
        return location;
    }

    public MenuList getMenuList() {
        return menuList;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

//    public String receiveOrders(String foodName, int quantity){
//        Order order = new Order(foodName,quantity);
//        orderList.addOrders(order);
//    }
}
