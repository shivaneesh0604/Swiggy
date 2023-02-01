package application;

import library.Notification;
import library.Order;
import library.Rider;

import java.util.ArrayList;
import java.util.Scanner;

 final class RiderUI implements UI {
    Scanner sc = new Scanner(System.in);
    private final Rider rider;

    public RiderUI(Rider rider) {
        this.rider = rider;
    }

    public void entersUI() {
        ArrayList<Notification> notification = rider.getNotification();
        showAvailableNotifications(notification);
        System.out.println("enter orderID to accept");
        int orderID = sc.nextInt();
        System.out.println("press 1 to accept and 2 to decline");
        int acceptance = sc.nextInt();
        if (acceptance == 1) {
            for (Notification notification1 : notification) {
                if (notification1.getOrderList().getOrderID() == orderID) {
                    System.out.println(rider.acceptOrder(notification1));
                    break;
                }
            }
        } else if (acceptance == 2) {
            for (Notification notification1 : notification) {
                if (notification1.getOrderList().getOrderID() == orderID) {
                    System.out.println(rider.declineOrder(notification1));
                    break;
                }
            }
        }
        System.out.println(rider.changeStatusToPicked());
        System.out.println(rider.changedStatusToDelivered());
    }

    private void showAvailableNotifications(ArrayList<Notification> notifications) {
        for (Notification notification : notifications) {
            Order order = notification.getOrderList();
            System.out.println(order.getOrderID() + " is the order ID " + order.getRestaurantID() + " is the restaurant ID " + order.getCustomerLocation() + " is the location of customer " + order.getRestaurantLocation() + " is the location of the restaurant " + order.getRestaurantName() + " is the name of the restaurant");
        }
    }

}
