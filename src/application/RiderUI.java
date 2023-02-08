package application;

import library.*;

import java.util.ArrayList;
import java.util.Scanner;

final class RiderUI implements UI {
    Scanner sc = new Scanner(System.in);
    private final Rider rider;

    RiderUI(Rider rider) {
        this.rider = rider;
    }

    public void entersUI() {

        System.out.println("enter in which location you are");
        Utils.print(Location.values());
        int location = Utils.inputVerification(Location.values().length);
        Location location1 = Location.values()[location];
        rider.setLocation(location1);
        MainLoop:
        while (true) {
            Utils.print(RiderOptions.values());
            int riderOption = Utils.inputVerification(RiderOptions.values().length);
            RiderOptions riderOptions1 = RiderOptions.values()[riderOption];
            switch (riderOptions1) {
                case SET_RIDER_STATUS:
                    System.out.println("your status now is " + rider.getRiderStatus());
                    Utils.print(RiderStatus.values());
                    int riderStatus = Utils.inputVerification(RiderStatus.values().length);
                    RiderStatus riderStatus1 = RiderStatus.values()[riderStatus];
                    if (rider.getOrder() == null) {
                        System.out.println("the status is"+rider.setRiderStatus(riderStatus1));
                    } else {
                        System.out.println("cant change because you have picked an order first deliver that order and change status");
                    }
                    break;

                case SHOW_AVAILABLE_NOTIFICATIONS:
                    if (rider.getRiderStatus().equals(RiderStatus.AVAILABLE)) {
                        ArrayList<Notification> notifications3 = rider.getNotification();
                        showAvailableNotifications(notifications3);
                    } else if (rider.getOrder() != null) {
                        System.out.println("you have already chosen an order complete that order first");
                    } else {
                        System.out.println("you cant do any functions since you had set you status as " + rider.getRiderStatus());
                    }
                    break;

                case ACCEPT_ORDER:
                    if (rider.getRiderStatus().equals(RiderStatus.AVAILABLE)) {
                        ArrayList<Notification> notifications = rider.getNotification();
                        showAvailableNotifications(notifications);
                        System.out.println("enter orderID to accept");
                        String orderID = sc.nextLine();
                        for (Notification notification1 : notifications) {
                            if (notification1.getOrder().getOrderID() == Integer.parseInt(orderID)) {
                                System.out.println(rider.acceptOrder(notification1));
                                break;
                            }
                        }
                    } else if (rider.getOrder() != null) {
                        System.out.println("you have already chosen an order complete that order first");
                    } else {
                        System.out.println("you cant do any functions since you had set you status as " + rider.getRiderStatus());
                    }
                    break;

                case DECLINE_ORDER:
                    if (rider.getRiderStatus().equals(RiderStatus.AVAILABLE)) {
                        ArrayList<Notification> notifications1 = rider.getNotification();
                        showAvailableNotifications(notifications1);
                        System.out.println("enter orderID to decline order");
                        String orderID1 = sc.nextLine();
                        for (Notification notification1 : notifications1) {
                            if (notification1.getOrder().getOrderID() == Integer.parseInt(orderID1)) {
                                System.out.println("this order is " + rider.declineOrder(notification1));
                                break;
                            }
                        }
                    } else if (rider.getOrder() != null) {
                        System.out.println("you have already chosen an order complete that order first");
                    } else {
                        System.out.println("you cant do any functions since you had set you status as " + rider.getRiderStatus() + " change your status to " + RiderStatus.AVAILABLE);
                    }
                    break;

                case CHANGE_STATUS_TO_PICKED:
                    if (rider.getOrder() == null) {
                        System.out.println("accept and order first ");
                    }
                    System.out.println(rider.changeStatusToPicked());
                    break;

                case CHANGE_STATUS_TO_DELIVERED:
                    if (rider.getOrder() == null) {
                        System.out.println("accept and order first ");
                    }
                    System.out.println(rider.changeStatusToDelivered());
                    break ;

                case BACK:
                    break MainLoop;
            }
        }
    }
//        showAvailableNotifications(notifications);
//        ArrayList<Notification> notifications = rider.getNotification();
//        showAvailableNotifications(notifications);
//        System.out.println("enter orderID to accept");
//        int orderID = sc.nextInt();
//        System.out.println("press 1 to accept and 2 to decline");
//        int acceptance = sc.nextInt();
//        if (acceptance == 1) {
//            for (Notification notification1 : notifications) {
//                if (notification1.getOrder().getOrderID() == orderID) {
//                    System.out.println(rider.acceptOrder(notification1));
//                    break;
//                }
//            }
//        } else if (acceptance == 2) {
//            for (Notification notification1 : notifications) {
//                if (notification1.getOrder().getOrderID() == orderID) {
//                    System.out.println(rider.declineOrder(notification1));
//                    break;
//                }
//            }
//        }
//        System.out.println(rider.changeStatusToPicked());
//        System.out.println(rider.changedStatusToDelivered());


    private void showAvailableNotifications(ArrayList<Notification> notifications) {
        if ((notifications != null) && (notifications.size() > 0)) {
            for (Notification notification : notifications) {
                Order order = notification.getOrder();
                System.out.println(order.getOrderID() + " is the order ID " + order.getRestaurantID() + " is the restaurant ID " + order.getCustomerLocation() + " is the location of customer " + order.getRestaurantLocation() + " is the location of the restaurant " + order.getRestaurantName() + " is the name of the restaurant");
            }
        } else {
            System.out.println("no orders available");
        }
    }
}
