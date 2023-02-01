package application;

import library.OrderList;
import library.Rider;

import java.util.ArrayList;
import java.util.Scanner;

public class RiderUI implements UI {
    Scanner sc = new Scanner(System.in);
    private final Rider rider;

    public RiderUI(Rider rider) {
        this.rider = rider;
    }

    public void entersUI() {
        System.out.println("enter orderID to accept");
        int orderID = sc.nextInt();
        System.out.println(rider.acceptOrder(orderID));
        System.out.println(rider.receiveOrderFromRestaurant());
        System.out.println(rider.cancelOrder());
        System.out.println("enter orderID to accept");
        int orderID2 = sc.nextInt();
        System.out.println(rider.acceptOrder(orderID2));
        System.out.println(rider.receiveOrderFromRestaurant());
        System.out.println(rider.deliverFood());
    }

    private void showAvailableOrders(ArrayList<OrderList> availableOrder) {
        if(availableOrder==null){
            System.out.println("cant accept order since you have already accepted one...delete that one to accept new order");
            return;
        }
        for (OrderList orderList : availableOrder) {
            System.out.println(orderList.getOrderID() + " is the order ID " + orderList.getRestaurantID() + " is the restaurant ID " + orderList.getCustomerLocation() + " is the location of customer " + orderList.getRestaurantLocation() + " is the location of the restaurant " + orderList.getRestaurantName() + " is the name of the restaurant");
        }
    }

}
