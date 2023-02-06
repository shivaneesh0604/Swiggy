package application;

import com.sun.deploy.ui.UITextArea;
import com.sun.org.apache.xpath.internal.operations.Or;
import library.*;
import org.xml.sax.helpers.AttributesImpl;

import javax.sound.sampled.Line;
import java.util.*;

final class CustomerUI implements UI {
    private final Customer customer;
    Scanner sc = new Scanner(System.in);

    CustomerUI(Customer customer) {
        this.customer = customer;
    }

    public void entersUI() {
        MainLoop:
        while (true) {
            System.out.println("enter from which location you are from");
            Utils.print(Location.values());
            int locationOption = Utils.inputVerification(Location.values().length);
            Location location = Location.values()[locationOption];
            customer.setLocation(location);
            System.out.println("enter Which Timing you are entering");
            Utils.print(Timing.values());
            int timingOption = Utils.inputVerification(Timing.values().length);
            Timing timing = Timing.values()[timingOption];
            SecondLoop:
            while (true) {
                HashMap<Integer, Restaurant> listOfRestaurants = customer.getAllRestaurant(location);
                showAllRestaurant(listOfRestaurants);
                System.out.println("enter which restaurantID to enter");
                String restaurantID = sc.nextLine();
                if (listOfRestaurants.containsKey(Integer.parseInt(restaurantID))) {
                    HashMap<String, Item> items = customer.enterRestaurant(listOfRestaurants.get(Integer.parseInt(restaurantID)), timing);
                    Collection<Item> items2 = items.values();
                    CustomerOption:
                    while (true) {
                        Utils.print(CustomerOptions.values());
                        int customerOption = Utils.inputVerification(CustomerOptions.values().length);
                        CustomerOptions customerOptions1 = CustomerOptions.values()[customerOption];
                        switch (customerOptions1) {
                            case ADD_ORDER:
                                showAvailableMenu(items2);
                                System.out.println("food items ordered are");
                                HashMap<Integer, Order> cartItems1 = customer.viewItemsInCart();
                                if (cartItems1 == null) {
                                    System.out.println("no cart items found");
                                } else {
                                    showCartItems(cartItems1.get(Integer.parseInt(restaurantID)));
                                }
                                System.out.println("enter which food to add");
                                String foodName = sc.nextLine().toUpperCase();
                                Order order = cartItems1.get(Integer.parseInt(restaurantID));
                                if (items.containsKey(foodName)) {
                                    if (order == null) {
                                        System.out.println(customer.addOrder(items.get(foodName), listOfRestaurants.get(Integer.parseInt(restaurantID))));
                                    }
                                    else {
                                        System.out.println("your cart contains dishes from  "+order.getRestaurantID()+" do you want to discard that selection and add dishes from"+ restaurantID+ "... if yes press 1 else other");
                                        String orderConfirmation = sc.nextLine();
                                        if(Integer.parseInt(orderConfirmation)==1){
                                            System.out.println(customer.addOrder(items.get(foodName), listOfRestaurants.get(Integer.parseInt(restaurantID))));
                                        }
                                        else {
                                            System.out.println("you cant order in this restaurant");
                                            break ;
                                        }
                                    }
                                } else {
                                    System.out.println("wrong foodName to add");
                                }
                                break;

                            case REMOVE_ORDER:
                                System.out.println("food items ordered are");
                                HashMap<Integer, Order> cartItems2 = customer.viewItemsInCart();
                                Order cartItems = cartItems2.get(Integer.parseInt(restaurantID));
                                showCartItems(cartItems);
                                if (cartItems != null) {
                                    showCartItems(cartItems);
                                    System.out.println("enter which food to delete");
                                    String foodName1 = sc.nextLine().toUpperCase();
                                    if (items.containsKey(foodName1)) {
                                        System.out.println(customer.removeOrder(items.get(foodName1), listOfRestaurants.get(Integer.parseInt(restaurantID))));
                                    } else {
                                        System.out.println("wrong foodName to delete");
                                    }
                                } else {
                                    System.out.println("no items in cart");
                                }
                                break;

                            case VIEW_ITEMS_IN_CART:
                                Order cartItems3 = customer.viewItemsInCart().get(Integer.parseInt(restaurantID));
                                showCartItems(cartItems3);
                                break;

                            case CONFIRM_ORDER:
                                Bill bill = customer.confirmOrder(listOfRestaurants.get(Integer.parseInt(restaurantID)));
                                showBill(bill);
                                break;

                            case PLACE_ORDER:
                                OrderStatus orderstatus = customer.placeOrder(listOfRestaurants.get(Integer.parseInt(restaurantID)));
                                System.out.println("order placed and status of food is " + orderstatus);
                                break;

                            case VIEW_ORDER_PLACED:
                                HashMap<Integer, ArrayList<Order>> orders = customer.viewOrdersPlaced();
                                Collection<ArrayList<Order>> ordersPlaced = orders.values();
                                viewOrder(ordersPlaced);
                                break;

                            case CANCEL_ORDER:
                                HashMap<Integer, ArrayList<Order>> orders1 = customer.viewOrdersPlaced();
                                Collection<ArrayList<Order>> ordersPlaced1 = orders1.values();
                                viewOrder(ordersPlaced1);
                                try {
                                    System.out.println("enter orderID to cancelOrder");
                                    String orderID = sc.nextLine();
                                    for (ArrayList<Order> ordersPlaced3 : ordersPlaced1) {
                                        for (Order order1 : ordersPlaced3) {
                                            if (order1.getOrderID() == Integer.parseInt(orderID)) {
                                                customer.cancelOrder(Integer.parseInt(orderID));
                                                break;
                                            }
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("number format exception enter integer value");
                                }
                                break;

                            case CHECK_STATUS_OF_ORDER:
                                HashMap<Integer, ArrayList<Order>> orders2 = customer.viewOrdersPlaced();
                                Collection<ArrayList<Order>> ordersPlaced2 = orders2.values();
                                viewOrder(ordersPlaced2);
                                try {
                                    System.out.println("enter orderID to ");
                                    String orderID1 = sc.nextLine();
                                    for (ArrayList<Order> ordersPlaced3 : ordersPlaced2) {
                                        for (Order order2 : ordersPlaced3) {
                                            if ((order2.getOrderID() == Integer.parseInt(orderID1))) {
                                                System.out.println(customer.checkStatusOfOrder(Integer.parseInt(orderID1)));
                                                break;
                                            }
                                        }

                                    }
                                    System.out.println("no orders found with this orderID");
                                } catch (NumberFormatException e) {
                                    System.out.println("number format exception enter integer value");
                                }
                                break;

                            case GO_BACK:
                                break SecondLoop;
                        }
                    }
                } else {
                    System.out.println("wrong restaurantID");
                }
            }
            System.out.println("press 1 to go back of application");
            String back = sc.nextLine();
            if (Integer.parseInt(back) == 1) {
                break MainLoop;
            } else continue;
        }
    }
//        Location location = Location.AREA1;
//        customer.setLocation(location);
//        MainLoop:while (true) {
//            HashMap<Integer, Restaurant> listOfRestaurants = customer.getAllRestaurant(location);
//            showAllRestaurant(listOfRestaurants);
//            System.out.println("enter which restaurantID to enter");
//            int restaurantID = 1;
//            if (listOfRestaurants.containsKey(restaurantID)) {
//                System.out.println("enter Which Timing you are entering");
//                Timing timing = Timing.AFTERNOON;
//                HashMap<String, Item> items = customer.enterRestaurant(listOfRestaurants.get(restaurantID), timing);
//                Collection<Item> items2 = items.values();
//                showAvailableMenu(items2);
//                String foodName = "chicken";
//                String foodName2 = "rice";
//                int quantity = 2;
//                for (Item item : items2) {
//                    if (item.getFoodName().equals(foodName.toUpperCase())) {
//                        System.out.println(customer.addOrder(item, quantity, listOfRestaurants.get(restaurantID)));
//                    }
//                }
//                for (Item item : items2) {
//                    if (item.getFoodName().equals(foodName2.toUpperCase())) {
//                        System.out.println(customer.addOrder(item, quantity, listOfRestaurants.get(restaurantID)));
//                    }
//                }
//                System.out.println("deleting orders");
//                for (Item item : items2) {
//                    if (item.getFoodName().equals(foodName.toUpperCase())) {
//                        System.out.println(customer.removeOrder(item, quantity, listOfRestaurants.get(restaurantID)));
//                    }
//                }
//                System.out.println("bill is");
//                Bill bill = customer.confirmOrder(listOfRestaurants.get(restaurantID));
//                showBill(bill);
//                System.out.println(customer.placeOrder(listOfRestaurants.get(restaurantID)));
//                ArrayList<Order> orders = customer.viewOrdersPlaced();
//                viewOrder(orders);
//                break;
//            }
//        System.out.println("enter orderID to cancel");
//        int orderID = sc.nextLine();
//        System.out.println(customer.cancelOrder(orderID));


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

    private void showCartItems(Order cartItems) {
        HashMap<String, LineOrder> lineOrders = cartItems.getOrders();
        Collection<LineOrder> lineOrders1 = lineOrders.values();
        for (LineOrder lineOrder : lineOrders1) {
            System.out.println("foodName is " + lineOrder.getItem().getFoodName() + " quantity is " + lineOrder.getQuantity());
        }
    }

    private void showBill(Bill bill) {
        ArrayList<Bill.BillItem> items = bill.getItems();
        for (Bill.BillItem billItem : items) {
            System.out.println(billItem.getItemName() + " " + billItem.getQuantity());
        }
        System.out.println("the total is " + bill.total());
    }

    private void viewOrder(Collection<ArrayList<Order>> ordersPlaced) {
        for (ArrayList<Order> orders : ordersPlaced) {
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
}