package library;

import library.MenuList;
import library.Order;

import java.util.ArrayList;
import java.util.HashMap;

public class Restaurant {
    private final String location;
    private final MenuList menuList;
    private final String restaurantName;
    private final int restaurantID;
    private final HashMap<Integer, ArrayList<Order>> orders;//orderID mapped with orders

    public Restaurant(String location, String restaurantName, int restaurantID) {
        this.location = location;
        this.restaurantName = restaurantName;
        this.restaurantID = restaurantID;
        this.menuList = new MenuList();
        this.orders = new HashMap<>();
    }

    void receiveOrders(int orderID, ArrayList<Order> orders) {
        this.orders.put(orderID, orders);
    }


    HashMap<Integer, ArrayList<Order>> viewOrder() {
        return orders;
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
