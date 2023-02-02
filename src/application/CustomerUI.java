package application;

import library.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

final class CustomerUI implements UI {
    private final Customer customer;
    Scanner sc = new Scanner(System.in);

    CustomerUI(Customer customer) {
        this.customer = customer;
    }

    public void entersUI() {
        MainLoop:
        System.out.println("enter from which location you are from");
        Location location = Location.AREA1;
        customer.setLocation(location);
        while (true) {
            HashMap<Integer, Restaurant> listOfRestaurants = customer.getAllRestaurant(location);
            showAllRestaurant(listOfRestaurants);
            System.out.println("enter which restaurantID to enter");
            int restaurantID = 1;
            if (listOfRestaurants.containsKey(restaurantID)) {
                System.out.println("enter Which Timing you are entering");
                Timing timing = Timing.AFTERNOON;
                HashMap<String, Item> items = customer.enterRestaurant(listOfRestaurants.get(restaurantID), timing);
                Collection<Item> items2 = items.values();
                showAvailableMenu(items2);
                String foodName = "chicken";
                String foodName2 = "rice";
                int quantity = 2;
                for (Item item : items2) {
                    if (item.getFoodName().equals(foodName.toUpperCase())) {
                        System.out.println(customer.addOrder(item, quantity, listOfRestaurants.get(restaurantID)));
                    }
                }
                for (Item item : items2) {
                    if (item.getFoodName().equals(foodName2.toUpperCase())) {
                        System.out.println(customer.addOrder(item, quantity, listOfRestaurants.get(restaurantID)));
                    }
                }
                System.out.println("deleting orders");
                for (Item item : items2) {
                    if (item.getFoodName().equals(foodName.toUpperCase())) {
                        System.out.println(customer.removeOrder(item, quantity, listOfRestaurants.get(restaurantID)));
                    }
                }
                System.out.println("bill is");
                Bill bill = customer.confirmOrder(listOfRestaurants.get(restaurantID));
                showBill(bill);
                System.out.println(customer.placeOrder(listOfRestaurants.get(restaurantID)));
                ArrayList<Order> orders = customer.viewOrdersPlaced();
                viewOrder(orders);
                break;
            }
//        System.out.println("enter orderID to cancel");
//        int orderID = sc.nextInt();
//        System.out.println(customer.cancelOrder(orderID));
        }
    }

    private void showAllRestaurant(HashMap<Integer, Restaurant> listOfRestaurant) {
        Collection<Restaurant> listOfRestaurants = listOfRestaurant.values();
        for (Restaurant restaurant : listOfRestaurants) {
            System.out.println(restaurant.getRestaurantID() + " " + restaurant.getRestaurantName() + " " + restaurant.getLocation());
        }
    }

    private void showAvailableMenu(Collection<Item> availableItems) {
        for (Item item : availableItems) {
            System.out.println(item.getFoodName() + " " + item.getPrice());
        }
    }

    private void showBill(Bill bill) {
        ArrayList<Bill.BillItem> items = bill.getItems();
        for (Bill.BillItem billItem : items) {
            System.out.println(billItem.getItemName() + " " + billItem.getQuantity());
        }
        System.out.println("the total is " + bill.total());
    }

    private void viewOrder(ArrayList<Order> orders) {
        for (Order order : orders) {
            System.out.println("orderID is " + order.getOrderID());
            HashMap<String, LineOrder> orderInOrderList = order.getOrders();
            Collection<LineOrder> lineOrderCollection = orderInOrderList.values();
            for (LineOrder lineOrder : lineOrderCollection) {
                System.out.println(" ordered food are " + lineOrder.getItem().getFoodName() + " quantity is " + lineOrder.getQuantity());
            }
        }
    }
}