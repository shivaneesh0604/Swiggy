package library;

import java.util.ArrayList;
import java.util.HashMap;

public interface CustomerApplication {
    public HashMap<String, Item> enterRestaurant(int restaurantID, Timing timing);
    HashMap<Integer, String> getAllRestaurant();

    public String takeOrder(String foodName, int quantity, String customerID, int restaurantID);

    public String deleteOrder(String foodName, int quantity, String customerID, int restaurantID);

    public HashMap<Integer, OrderList> viewItemsInCart(String customerID);

    public Bill confirmOrder(String customerID, int restaurantID);

    public Status placeOrder(String customerID, int restaurantID,Location location);

    public String checkStatusOfOrder(int orderID);

    public ArrayList<OrderList> viewOrdersPlaced(String customerID);

    public Status cancelOrder(int orderID);
}
