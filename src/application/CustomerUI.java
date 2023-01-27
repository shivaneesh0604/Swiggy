package application;

import library.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

public class CustomerUI implements UI {
    private final Customer customer;
    Scanner sc = new Scanner(System.in);

    public CustomerUI(Customer customer) {
        this.customer = customer;
    }

    public void entersUI() {
        MainLoop:
        while (true) {
            System.out.println("enter from which location you are from");
            String location = "adayar";
            customer.setLocation(location);
            HashMap<Integer, String> listOfRestaurants = Database.getInstance().getAllRestaurant();
            showAllRestaurant(listOfRestaurants);
            System.out.println("enter which restaurantID to enter");
            int restaurantID = 1;
            System.out.println("enter Which Timing you are entering");
            Timing timing = Timing.AFTERNOON;
            showAvailableMenu(customer.enterRestaurant(restaurantID, timing));
            String foodName = "chicken";
            String foodName2 = "rice";
            int quantity = 2;
            System.out.println(customer.addOrders(foodName, quantity, restaurantID));
            System.out.println(customer.addOrders(foodName2, quantity, restaurantID));
            System.out.println(customer.deleteOrders(foodName, 1, restaurantID));
            Bill bill = customer.confirmOrder(restaurantID);
            showBill(bill);
            System.out.println(customer.placeOrder(restaurantID));
            ArrayList<OrderList> orders = customer.viewOrder();
            viewOrder(orders);
            break ;
//        System.out.println("enter orderID to cancel");
//        int orderID = sc.nextInt();
//        System.out.println(customer.cancelOrder(orderID));
        }
    }

    private void showAllRestaurant(HashMap<Integer, String> listOfRestaurants) {
        System.out.println(listOfRestaurants);
    }

    private void showAvailableMenu(HashMap<String, Item> availableItems) {
        Collection<Item> items = availableItems.values();
        for (Item item : items) {
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

    private void viewOrder(ArrayList<OrderList> order) {
        for (OrderList orderList : order) {
            System.out.println("orderID is " + orderList.getOrderID());
            ArrayList<Order> orderInOrderList = orderList.getOrders();
            for (Order order1 : orderInOrderList) {
                System.out.println(" ordered food are " + order1.getFoodName() + " quantity is " + order1.getQuantity());
            }
        }
    }
}