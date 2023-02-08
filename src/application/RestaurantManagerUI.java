package application;

import library.LineOrder;
import library.OrderStatus;
import library.RestaurantManager;
import library.RestaurantStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

 final class RestaurantManagerUI implements UI {
    Scanner sc = new Scanner(System.in);
    private final RestaurantManager restaurantManager;

    RestaurantManagerUI(RestaurantManager restaurantManager) {
        this.restaurantManager = restaurantManager;
    }

    public void entersUI() {
//        System.out.println(restaurantManager.setRestaurantStatus(RestaurantStatus.NOT_AVAILABLE));
        HashMap<Integer, ArrayList<LineOrder>> order = restaurantManager.viewOrderGot();
        System.out.println("orders got are");
        viewOrder(order);
        System.out.println("enter orderID to prepared");
        int orderID = sc.nextInt();
        System.out.println(restaurantManager.setStatus(orderID, OrderStatus.PREPARING));
        System.out.println(restaurantManager.setStatus(orderID,OrderStatus.PREPARED));
    }

    private void viewOrder(HashMap<Integer, ArrayList<LineOrder>> order) {
        if(order==null){
            System.out.println("no orders found ");
            return;
        }
        for (Map.Entry<Integer, ArrayList<LineOrder>> entry : order.entrySet()) {
            ArrayList<LineOrder> lineOrder1 = entry.getValue();
            System.out.println("orderID is : " + entry.getKey());
            for (LineOrder lineOrder2 : lineOrder1) {
            }
        }
    }
}
