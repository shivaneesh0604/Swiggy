package library;

import java.util.ArrayList;
import java.util.HashMap;

public interface CustomerApplication {

    public HashMap<String,Item> enterRestaurant(int restaurantID, Timing timing);
    public HashMap<Integer,OrderList> viewItemsInCart(String customerID);
    public ArrayList<Order> viewCartItems(int restaurantID, String customerID);
    public String TakeOrder( String foodName,int quantity,String customerID,int restaurantID);
    public String deleteOrder( String foodName,int quantity,String customerID,int restaurantID);
    public Status confirmOrder(String customerID,int restaurantID);
    public Bill getBill(String customerID, int restaurantID);
    public Status checkStatus(int orderID);
    public OrderList viewOrder(String customerID);
    public Status cancelOrder(int orderID);

}
