package application;

import javafx.application.Application;
import library.*;

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
                Utils.print(CustomerOptions.values());
                int customerInput = Utils.inputVerification(CustomerOptions.values().length);
                CustomerOptions customerOptions = CustomerOptions.values()[customerInput];
                switch (customerOptions) {
                    case ENTERS_RESTAURANT:
                        enterRestaurant(location, timing);
                        break;
                    case VIEW_ORDER_PLACED:
                        viewOrder(customer.viewOrdersPlaced());
                        break;
                    case CANCEL_ORDER:
                        viewCancelledOrder();
                        break;
                    case GO_BACK:
                        break SecondLoop;
                }
            }
            System.out.println("press 1 to go back of application press another number to go back again to customer Options");
            String back = sc.nextLine();
            if (Integer.parseInt(back) == 1) {
                break;
            }
        }
    }

    private void enterRestaurant(Location location, Timing timing) {
        try {
            HashMap<Integer, Restaurant> listOfRestaurants = customer.getAllRestaurant(location);
            showAllRestaurant(listOfRestaurants);
            System.out.println("enter which restaurantID to enter");
            String restaurantID = sc.nextLine();
            if (!listOfRestaurants.containsKey(Integer.parseInt(restaurantID))) {
                System.out.println("wrong restaurantID");
                return;
            }
            CustomerOrderOptions:
            while (true) {
                HashMap<String, Item> items = customer.enterRestaurant(listOfRestaurants.get(Integer.parseInt(restaurantID)), timing);
                Utils.print(CustomerOrderOptions.values());
                int customerOrderInput = Utils.inputVerification(CustomerOrderOptions.values().length);
                CustomerOrderOptions customerOrderOptions = CustomerOrderOptions.values()[customerOrderInput];
                switch (customerOrderOptions) {
                    case ADD_ORDER: {

                        showAvailableMenu(items.values());
                        HashMap<Integer, Order> cartItems = customer.viewItemsInCart();

                        if (cartItems == null || cartItems.size() == 0) {
                            System.out.println("no cart items found");
                        } else {
                            showCartItems(cartItems.get(Integer.parseInt(restaurantID)));
                        }
                        System.out.println("enter which food to add");
                        String foodName = sc.nextLine().toUpperCase();

                        if (!items.containsKey(foodName)) {
                            System.out.println("wrong foodName to add");
                            continue;
                        }
                        if (cartItems == null || cartItems.size()==0) {
                            System.out.println(customer.addOrder(items.get(foodName), listOfRestaurants.get(Integer.parseInt(restaurantID))));
                            break;
                        }

                        System.out.println(OrderAddition.ANOTHER_ORDER_ADDED_IN_A_RESTAURANT);
                        Order order = cartItems.get(Integer.parseInt(restaurantID));

                        System.out.println("your cart contains dishes from  " + order.getRestaurantID() + " do you want to discard that selection and add dishes from" + restaurantID + "... if yes press 1 else other");
                        String orderConfirmation = sc.nextLine();

                        if (Integer.parseInt(orderConfirmation) == 1) {
                            System.out.println(customer.addOrder(items.get(foodName), listOfRestaurants.get(Integer.parseInt(restaurantID))));
                            break;
                        }

                        System.out.println("you cant order in this restaurant");
                        break;

                    }

                    case REMOVE_ORDER: {
                        HashMap<Integer, Order> cartItems = customer.viewItemsInCart();
                        if (cartItems == null || cartItems.size() == 0) {
                            System.out.println("no items in cart");
                            break;
                        }
                        Order order = cartItems.get(Integer.parseInt(restaurantID));
                        showCartItems(order);
                        System.out.println("enter which food to delete");
                        String foodName1 = sc.nextLine().toUpperCase();
                        if (items.containsKey(foodName1)) {
                            System.out.println(customer.removeOrder(items.get(foodName1), listOfRestaurants.get(Integer.parseInt(restaurantID))));
                        } else {
                            System.out.println("wrong foodName to delete");
                        }
                        break;
                    }

                    case VIEW_ITEMS_IN_CART: {
                        HashMap<Integer, Order> cartItems = customer.viewItemsInCart();
                        if (cartItems == null || cartItems.size() == 0) {
                            System.out.println("no cart items found");
                        } else {
                            showCartItems(cartItems.get(Integer.parseInt(restaurantID)));
                        }
                        break;
                    }

                    case CONFIRM_ORDER: {
                        HashMap<Integer, Order> cartItems = customer.viewItemsInCart();
                        if (cartItems == null || cartItems.size()==0) {
                            System.out.println("no orders found first add an order");
                            break;
                        }

                        Bill bill = customer.confirmOrder(listOfRestaurants.get(Integer.parseInt(restaurantID)));
                        if(bill==null){
                            System.out.println("no orders found first add an order");
                            break ;
                        }
                        showBill(bill);

                        System.out.println("press 1 to place order");
                        String pay_Bill = sc.nextLine();
                        try {
                            if (Integer.parseInt(pay_Bill) != 1) {
                                System.out.println("not placed the order");
                                break;
                            }

                            HashMap<Integer, Order> cartItems6 = customer.viewItemsInCart();
                            if (cartItems6 == null) {
                                System.out.println("no orders found first add an order");
                                break;
                            }
                            OrderStatus orderstatus = customer.placeOrder(listOfRestaurants.get(Integer.parseInt(restaurantID)));
                            System.out.println("order placed and status of food is " + orderstatus);
                        } catch (NumberFormatException e) {
                            System.out.println("enter numbers to process");
                            continue;
                        }
                        break;
                    }

                    case BACK:
                        break CustomerOrderOptions;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("wrong restaurant ID entered enter the available numbers");
        }
    }

    private void viewCancelledOrder() {
        ArrayList<Order> ordersPlaced1 = customer.viewOrdersPlaced();
        viewOrder(ordersPlaced1);
        if (ordersPlaced1 != null) {
            System.out.println("enter orderID to cancelOrder");
            String orderID = sc.nextLine();
            for (Order order1 : ordersPlaced1) {
                if (order1.getOrderID() == Integer.parseInt(orderID)) {
                    if (order1.getStatus().equals(OrderStatus.PREPARED)) {
                        System.out.println("you will be charged with 50% of bill amount press 1 to cancel the order other value to wait");
                        String orderDeletion = sc.nextLine();
                        if (Integer.parseInt(orderDeletion) == 1) {
                            System.out.println(customer.cancelOrder(Integer.parseInt(orderID)));
                        }
                        break;
                    } else if (order1.getStatus().equals(OrderStatus.ORDER_PLACED)) {
                        System.out.println(customer.cancelOrder(Integer.parseInt(orderID)));
                    }
                }
            }
        }
    }

    private void showAllRestaurant(HashMap<Integer, Restaurant> listOfRestaurant) {
        if(listOfRestaurant.size() == 0){
            System.out.println("no restaurants found in this timing");
            return;
        }
        for (Restaurant restaurant : listOfRestaurant.values()) {
            System.out.println(restaurant.getRestaurantID() + " " + restaurant.getRestaurantName() + " " + restaurant.getLocation());
        }
    }

    private void showAvailableMenu(Collection<Item> availableItems) {
        for (Item item : availableItems) {
            System.out.println(item.getFoodName() + " " + item.getPrice());
        }
    }

    private void showCartItems(Order cartItems) {
        System.out.println("food items ordered are");
        HashMap<String, LineOrder> lineOrders = cartItems.getOrders();
        for (LineOrder lineOrder : lineOrders.values()) {
            System.out.println("foodName is " + lineOrder.getItem().getFoodName() + " quantity is " + lineOrder.getQuantity());
        }
    }

    private void showBill(Bill bill) {
        ArrayList<Bill.BillItem> items = bill.getItems();
        System.out.println("food name    quantity   price");
        for (Bill.BillItem billItem : items) {
            System.out.println(billItem.getItemName() + " " + billItem.getQuantity()+" "+billItem.getPrice());
        }
        System.out.println("the total is " + bill.total());
    }

    private void viewOrder(ArrayList<Order> ordersPlaced) {
        if (ordersPlaced != null) {
            for (Order order : ordersPlaced) {
                System.out.println("orderID is " + order.getOrderID());
                HashMap<String, LineOrder> orderInOrderList = order.getOrders();
                Collection<LineOrder> lineOrderCollection = orderInOrderList.values();
                for (LineOrder lineOrder : lineOrderCollection) {
                    System.out.println(" ordered food are " + lineOrder.getItem().getFoodName() + " quantity is " + lineOrder.getQuantity());
                }
                if (order.getRiderName() == null) {
                    System.out.println("ordered shop name is " + order.getRestaurantName() + " status of food is " + order.getStatus() + " rider acceptance status is " + order.getRiderFunctionalityStatus());
                } else {
                    System.out.println("ordered shop name is " + order.getRestaurantName() + " status of food is " + order.getStatus() + " rider acceptance status is " + order.getRiderFunctionalityStatus() + " rider name is " + order.getRiderName());
                }
            }
        } else {
            System.out.println("no orders found");
        }
    }

}