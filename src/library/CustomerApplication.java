package library;

import java.util.ArrayList;
import java.util.HashMap;

interface CustomerApplication {
    HashMap<Integer, Restaurant> getAllRestaurant(Location location);

    HashMap<String, Item> enterRestaurant(Restaurant restaurant, Timing timing);

    OrderAddition takeOrder(Item item, String customerID, Restaurant restaurant);

    OrderDeletion removeOrder(Item item, String customerID,Restaurant restaurant);

    HashMap<Integer, Order> viewItemsInCart(String customerID);

    Bill confirmOrder(String customerID, Restaurant restaurant);

    OrderStatus placeOrder(String customerID, Restaurant restaurant, Location location);

    ArrayList<Order> viewOrdersPlaced(String customerID);

    OrderStatus cancelOrder(int orderID);
}
