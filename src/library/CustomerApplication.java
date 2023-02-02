package library;

import java.util.ArrayList;
import java.util.HashMap;

interface CustomerApplication {
    HashMap<Integer, Restaurant> getAllRestaurant(Location location);

    HashMap<String, Item> enterRestaurant(Restaurant restaurant, Timing timing);

    String takeOrder(Item item, int quantity, String customerID, Restaurant restaurant);

    String removeOrder(Item item, int quantity, String customerID,Restaurant restaurant);

    HashMap<Integer, Order> viewItemsInCart(String customerID);

    Bill confirmOrder(String customerID, Restaurant restaurant);

    OrderStatus placeOrder(String customerID, Restaurant restaurant, Location location);

    String checkStatusOfOrder(int orderID);

    ArrayList<Order> viewOrdersPlaced(String customerID);

    OrderStatus cancelOrder(int orderID);
}
