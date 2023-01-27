package application;

import library.Order;
import library.RestaurantManager;
import library.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RestaurantManagerUI implements UI {
    Scanner sc = new Scanner(System.in);
    private final RestaurantManager restaurantManager;

    public RestaurantManagerUI(RestaurantManager restaurantManager) {
        this.restaurantManager = restaurantManager;
    }

    public void entersUI() {
        HashMap<Integer, ArrayList<Order>> order = restaurantManager.viewOrder();
        viewOrder(order);
        System.out.println("enter orderID to set status as prepared");
        int orderID = sc.nextInt();
        System.out.println(restaurantManager.setStatus(orderID));
    }

    private void viewOrder(HashMap<Integer, ArrayList<Order>> order) {
        if(order==null){
            System.out.println("no orders found ");
            return;
        }
        for (Map.Entry<Integer, ArrayList<Order>> entry : order.entrySet()) {
            ArrayList<Order> order1 = entry.getValue();
            System.out.println("orderID is : " + entry.getKey());
            for (Order order2 : order1) {
                System.out.println(" foodName is: " + order2.getFoodName()+" quantity is "+order2.getQuantity());
            }
        }
    }
}
