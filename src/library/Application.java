package library;

import java.util.ArrayList;
import java.util.HashMap;

public class Application implements CustomerApplication, RiderApplication, RestaurantManagerApplication {

    private final HashMap<String, HashMap<Integer, OrderList>> cartItems = new HashMap<>();//customerID->restaurantID,orderList
    int orderID = 1000;

    @Override
    public HashMap<String, Item> enterRestaurant(int restaurantID, Timing timing) {
        Restaurant restaurant = Database.getInstance().getRestaurant(restaurantID);
        return restaurant.getMenuList().getItems(timing);
    }


    @Override
    public String takeOrder(String foodName, int quantity, String customerID, int restaurantID) {
        HashMap<Integer, OrderList> orderList1 = cartItems.get(customerID);
        Order order = new Order(foodName, quantity);
        double price = Database.getInstance().getPrice(foodName.toUpperCase(), restaurantID);
        Customer customer = (Customer) Database.getInstance().getUser(customerID);
        Restaurant restaurant = Database.getInstance().getRestaurant(restaurantID);
        if (orderList1 != null) {
            OrderList orderList = orderList1.get(restaurantID);
            if (orderList.getRestaurantID() == restaurantID) {
                if (orderList != null) {
                    orderList.addOrders(order, price);
                } else {
                    OrderList orderList2 = new OrderList(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customer.getLocation(), customer.getUserID());
                    orderList1.put(restaurantID, orderList2);
                    orderList2.addOrders(order, price);
                }
            } else {
                orderList1.clear();
                OrderList orderList2 = new OrderList(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customer.getLocation(), customer.getUserID());
                orderList1.put(restaurantID, orderList2);
                orderList2.addOrders(order, price);
            }
        } else {
            HashMap<Integer, OrderList> orderList2 = new HashMap<>();
            OrderList orderList = new OrderList(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customer.getLocation(), customer.getUserID());
            orderList.addOrders(order, price);
            orderList2.put(restaurantID, orderList);
            cartItems.put(customerID, orderList2);
        }
        return "order added with name " + foodName + " with quantity " + quantity;
    }

    @Override
    public String deleteOrder(String foodName, int quantity, String customerID, int restaurantID) {
        OrderList orders = cartItems.get(customerID).get(restaurantID);
        if (orders != null) {
            return orders.deleteOrder(foodName, quantity);
        }
        return "no orders found with this foodName";
    }

    @Override
    public HashMap<Integer, OrderList> viewItemsInCart(String customerID) {
        if (cartItems.containsKey(customerID)) {
            return cartItems.get(customerID);
        }
        return null;
    }

    @Override
    public Bill confirmOrder(String customerID, int restaurantID) {
        OrderList orderList = cartItems.get(customerID).get(restaurantID);
        if (orderList != null) {
            Bill bill = orderList.getBill();
            for (Order order : orderList.getOrders()) {
                double price = Database.getInstance().getPrice(order.getFoodName(), restaurantID);
                bill.addItem(order.getFoodName(), order.getQuantity(), price);
            }
            return bill;
        }
        return null;
    }

    @Override
    public Status placeOrder(String customerID, int restaurantID) {
        OrderList orderList2 = cartItems.get(customerID).get(restaurantID);
        if (orderList2 != null) {
            orderList2.setOrderID(orderID);
            orderID++;
            Database.getInstance().addOrder(customerID, orderList2, restaurantID);
            cartItems.remove(customerID);
            Restaurant restaurant = Database.getInstance().getRestaurant(restaurantID);
            restaurant.receiveOrders(orderList2.getOrderID(), orderList2.getOrders());
            return Database.getInstance().setStatus(Status.PREPARING, orderList2.getOrderID());
        }
        return null;
    }

    @Override
    public ArrayList<OrderList> viewOrder(String customerID) {
        return Database.getInstance().getOrders(customerID);
    }

    @Override
    public Status cancelOrder(int orderID) {
        return Database.getInstance().setStatus(Status.CANCELLED, orderID);
    }

    @Override
    public Status checkStatus(int orderID) {
        return Database.getInstance().getStatus(orderID);
    }

    @Override
    public ArrayList<OrderList> showAvailableOrders() {
        return Database.getInstance().getAllOrders();
    }

    @Override
    public OrderList acceptOrder(int orderID) {
        OrderList order = Database.getInstance().getOrderlist(orderID);
        if (order.getOrderID() == orderID && !order.getStatus().equals(Status.CANCELLED) && order.getRiderAcceptance().equals(RiderAcceptance.NOT_ACCEPTED)) {
            order.setRiderAcceptance(RiderAcceptance.ACCEPTED);
            return order;
        }
        return null;
    }

    @Override
    public Status receiveOrderFromRestaurant(int orderID) {
        return Database.getInstance().setStatus(Status.PICKED, orderID);
    }

    @Override
    public Status getStatus(int orderID) {
        return Database.getInstance().getStatus(orderID);
    }

    @Override
    public Status deliverFoodToCustomer(int orderID) {
        Status status = Database.getInstance().getStatus(orderID);
        if (!status.equals(Status.CANCELLED)) {
            return Database.getInstance().setStatus(Status.DELIVERED, orderID);
        }
        return null;
    }

    @Override
    public RiderAcceptance deleteOrder(int orderID) {
        OrderList order = Database.getInstance().getOrderlist(orderID);
        if (order.getOrderID() == orderID && !order.getStatus().equals(Status.CANCELLED) && order.getRiderAcceptance().equals(RiderAcceptance.ACCEPTED)) {
            order.setRiderAcceptance(RiderAcceptance.NOT_ACCEPTED);
            return RiderAcceptance.NOT_ACCEPTED;
        }
        return null;
    }

    @Override
    public Status setStatusPREPARED(int orderID) {
        return Database.getInstance().setStatus(Status.PREPARED, orderID);
    }

}