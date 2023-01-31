package library;

import java.util.ArrayList;
import java.util.HashMap;

public class Application implements CustomerApplication, RiderApplication, RestaurantManagerApplication {

    private final HashMap<String, HashMap<Integer, OrderList>> cartItems = new HashMap<>();//customerID->restaurantID,orderList

    @Override
    public HashMap<String, Item> enterRestaurant(int restaurantID, Timing timing) {
        Restaurant restaurant = Database.getInstance().getRestaurant(restaurantID);
        if (restaurant != null) {
            return restaurant.getMenuList().getItems(timing);
        }
        return null;
    }

    @Override
    public String takeOrder(String foodName, int quantity, String customerID, int restaurantID) {
        HashMap<Integer, OrderList> customerOrderList = cartItems.get(customerID);
        Order order = new Order(foodName, quantity);
        Customer customer = (Customer) Database.getInstance().getUser(customerID);
        Restaurant restaurant = Database.getInstance().getRestaurant(restaurantID);
        if (customerOrderList != null) {
            OrderList restaurantOrderList = customerOrderList.get(restaurantID);
            if (restaurantOrderList.getRestaurantID() == restaurantID) {
                if (restaurantOrderList != null) {
                    restaurantOrderList.addOrders(order);
                } else {
                    OrderList orderList2 = new OrderList(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customer.getLocation(), customer.getUserID());
                    customerOrderList.put(restaurantID, orderList2);
                    orderList2.addOrders(order);
                }
            } else {
                customerOrderList.clear();
                OrderList orderList2 = new OrderList(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customer.getLocation(), customer.getUserID());
                customerOrderList.put(restaurantID, orderList2);
                orderList2.addOrders(order);
            }
        } else {
            HashMap<Integer, OrderList> orderList2 = new HashMap<>();
            OrderList orderList = new OrderList(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customer.getLocation(), customer.getUserID());
            orderList.addOrders(order);
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
        return "no orders found in this restaurant for this customer";
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
            Database.getInstance().addOrder(customerID, orderList2, restaurantID);
            cartItems.remove(customerID);
            Restaurant restaurant = Database.getInstance().getRestaurant(restaurantID);
            restaurant.receiveOrders(orderList2.getOrderID(), orderList2.getOrders());
            return Database.getInstance().setStatus(Status.PREPARING, orderList2.getOrderID());
        }
        return null;
    }

    @Override
    public ArrayList<OrderList> viewOrdersPlaced(String customerID) {
        ArrayList<OrderList> orderLists = Database.getInstance().getOrders(customerID);
        if (orderLists != null) {
            return orderLists;
        }
        return null;
    }

    @Override
    public Status cancelOrder(int orderID) {
        return Database.getInstance().setStatus(Status.CANCELLED, orderID);
    }

    @Override
    public String checkStatus(int orderID) {
        OrderList orderList = Database.getInstance().getOrderlist(orderID);
        if (orderList != null) {
            return "the status of food is " + orderList.getStatus() + " and the rider Acceptance status is " + orderList.getRiderAcceptance();
        }
        return "wrong orderID";
    }

    @Override
    public ArrayList<OrderList> showAvailableOrders() {
        return Database.getInstance().getAllOrders();
    }

    @Override
    public OrderList acceptOrder(int orderID) {
        OrderList order = Database.getInstance().getOrderlist(orderID);
        if (order.getOrderID() == orderID && !order.getStatus().equals(Status.CANCELLED)) {
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
    public RiderAcceptance deleteOrder(OrderList orderList) {
        orderList.setRiderAcceptance(RiderAcceptance.NOT_ACCEPTED);
        if (orderList.getStatus().equals(Status.PICKED)) {
            orderList.setStatus(Status.PREPARED);
        } else {
            orderList.setStatus(Status.PREPARING);
        }
        return RiderAcceptance.NOT_ACCEPTED;
    }

    @Override
    public Status setStatusPrepared(int orderID) {
        return Database.getInstance().setStatus(Status.PREPARED, orderID);
    }

}