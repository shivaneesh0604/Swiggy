package application;

import library.OrderList;
import library.Rider;

import java.util.ArrayList;

public class RiderUI implements UI {
    private final Rider rider;

    public RiderUI(Rider rider) {
        this.rider = rider;
    }

    public void entersUI() {
        showAvailableOrders(rider.showAvailableOrders());
        int orderID = 1000;
        System.out.println(rider.acceptOrder(orderID));
        System.out.println(rider.receiveOrderFromRestaurant());
        System.out.println(rider.deliverFood());
    }

    private void showAvailableOrders(ArrayList<OrderList> availableOrder) {
        for (OrderList orderList : availableOrder) {
            System.out.println(orderList.getOrderID() + " is the order ID " + orderList.getRestaurantID() + " is the restaurant ID " + orderList.getCustomerLocation() + " is the location of customer " + orderList.getRestaurantLocation() + " is the location of the restaurant " + orderList.getRestaurantName() + " is the name of the restaurant");
        }
    }

}
