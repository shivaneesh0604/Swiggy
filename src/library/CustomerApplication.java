package library;

import java.util.ArrayList;
import java.util.HashMap;

interface CustomerApplication {
    HashMap<Integer, String> getAllRestaurant(Location location);

    HashMap<String, Item> enterRestaurant(int restaurantID, Timing timing);

    String takeOrder(Item item, int quantity, String customerID, int restaurantID);

    String removeOrder(Item item, int quantity, String customerID, int restaurantID);

    HashMap<Integer, Order> viewItemsInCart(String customerID);

    Bill confirmOrder(String customerID, int restaurantID);

    OrderStatus placeOrder(String customerID, int restaurantID, Location location);

    String checkStatusOfOrder(int orderID);

    ArrayList<Order> viewOrdersPlaced(String customerID);

    OrderStatus cancelOrder(int orderID);
}
