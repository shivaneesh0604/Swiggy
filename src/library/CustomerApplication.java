package library;

import java.util.ArrayList;
import java.util.HashMap;

interface CustomerApplication {
     HashMap<String, Item> enterRestaurant(int restaurantID, Timing timing);
    HashMap<Integer, String> getAllRestaurant();

     String takeOrder(Item item, int quantity, String customerID, int restaurantID);

     String deleteOrder(Item item, int quantity, String customerID, int restaurantID);

     HashMap<Integer, Order> viewItemsInCart(String customerID);

     Bill confirmOrder(String customerID, int restaurantID);

     OrderStatus placeOrder(String customerID, int restaurantID, Location location);

     String checkStatusOfOrder(int orderID);

     ArrayList<Order> viewOrdersPlaced(String customerID);

     OrderStatus cancelOrder(int orderID);
}
