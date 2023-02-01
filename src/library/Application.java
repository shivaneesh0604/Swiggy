package library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Application implements CustomerApplication, RiderApplication, RestaurantManagerApplication {

    private final HashMap<String, HashMap<Integer, Order>> cartItems = new HashMap<>();//customerID->restaurantID,orderList

    @Override
    public HashMap<String, Item> enterRestaurant(int restaurantID, Timing timing) {
        Restaurant restaurant = Database.getInstanceDatabase().getRestaurant(restaurantID);
        if (restaurant != null) {
            return restaurant.getMenuList().getItems(timing);
        }
        return null;
    }

    @Override
    public HashMap<Integer, String> getAllRestaurant() {
        return Database.getInstanceDatabase().getAllRestaurant();
    }

    @Override
    public String takeOrder(String foodName, int quantity, String customerID, int restaurantID) {
        HashMap<Integer, Order> customerOrderList = cartItems.get(customerID);
        LineOrder lineOrder = new LineOrder(foodName, quantity);
        CustomerDetails customerDetails =  Database.getInstanceDatabase().getCustomerDetails(customerID);
        Restaurant restaurant = Database.getInstanceDatabase().getRestaurant(restaurantID);
        if (customerOrderList != null) {
            Order restaurantOrder = customerOrderList.get(restaurantID);
            if (restaurantOrder.getRestaurantID() == restaurantID) {
                if (restaurantOrder != null) {
                    restaurantOrder.addOrders(lineOrder);
                } else {
                    Order order2 = new Order(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customerDetails.getLocation(), customerDetails.getUserID());
                    customerOrderList.put(restaurantID, order2);
                    order2.addOrders(lineOrder);
                }
            } else {
                customerOrderList.clear();
                Order order2 = new Order(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customerDetails.getLocation(), customerDetails.getUserID());
                customerOrderList.put(restaurantID, order2);
                order2.addOrders(lineOrder);
            }
        } else {
            HashMap<Integer, Order> orderList2 = new HashMap<>();
            Order order = new Order(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customerDetails.getLocation(), customerDetails.getUserID());
            order.addOrders(lineOrder);
            orderList2.put(restaurantID, order);
            cartItems.put(customerID, orderList2);
        }
        return "order added with name " + foodName + " with quantity " + quantity;
    }

    @Override
    public String deleteOrder(String foodName, int quantity, String customerID, int restaurantID) {
        Order orders = cartItems.get(customerID).get(restaurantID);
        if (orders != null) {
            return orders.deleteOrder(foodName, quantity);
        }
        return "no orders found in this restaurant for this customer";
    }

    @Override
    public HashMap<Integer, Order> viewItemsInCart(String customerID) {
        if (cartItems.containsKey(customerID)) {
            return cartItems.get(customerID);
        }
        return null;
    }

    @Override
    public Bill confirmOrder(String customerID, int restaurantID) {
        Order order = cartItems.get(customerID).get(restaurantID);
        if (order != null) {
            Bill bill = order.getBill();
            HashMap<String, LineOrder> orderInOrderList = order.getOrders();
            Collection<LineOrder> lineOrderCollection = orderInOrderList.values();
            for (LineOrder lineOrder : lineOrderCollection) {
                double price = Database.getInstanceDatabase().getPrice(lineOrder.getFoodName(), restaurantID);
                bill.addItem(lineOrder.getFoodName(), lineOrder.getQuantity(), price);
            }
            return bill;
        }
        return null;
    }

    @Override
    public Status placeOrder(String customerID, int restaurantID,Location location) {
        Order order2 = cartItems.get(customerID).get(restaurantID);
        if (order2 != null) {
            Database.getInstanceDatabase().addOrder(customerID, order2, restaurantID,location);
            cartItems.remove(customerID);
            Restaurant restaurant = Database.getInstanceDatabase().getRestaurant(restaurantID);
            HashMap<String, LineOrder> orderInOrderList = order2.getOrders();
            Collection<LineOrder> lineOrderCollection = orderInOrderList.values();
            restaurant.receiveOrders(order2.getOrderID(), (ArrayList<LineOrder>) lineOrderCollection);
            return Database.getInstanceDatabase().setStatus(Status.PREPARING, order2.getOrderID());
        }
        return null;
    }

    @Override
    public ArrayList<Order> viewOrdersPlaced(String customerID) {
        ArrayList<Order> orders = Database.getInstanceDatabase().getOrders(customerID);
        if (orders != null) {
            return orders;
        }
        return null;
    }

    @Override
    public Status cancelOrder(int orderID) {
        return Database.getInstanceDatabase().setStatus(Status.CANCELLED, orderID);
    }

    @Override
    public String checkStatusOfOrder(int orderID) {
        Order order = Database.getInstanceDatabase().getOrderlist(orderID);
        if (order != null) {
            return "the status of food is " + order.getStatus() + " and the rider Acceptance status is " + order.getRiderAcceptance();
        }
        return "wrong orderID";
    }

    @Override
    public RiderAcceptance acceptOrder(int orderID) {
        Order order = Database.getInstanceDatabase().getOrderlist(orderID);
        if (order.getOrderID() == orderID && !order.getStatus().equals(Status.CANCELLED)) {
            order.setRiderAcceptance(RiderAcceptance.ACCEPTED);
            return order.getRiderAcceptance();
        }
        return null;
    }

    @Override
    public RiderAcceptance declineOrder(Order order, Rider rider) {
        ArrayList<Rider> riders = Database.getInstanceDatabase().getAllRiders();
        for(Rider rider1:riders) {
            int index = Database.getInstanceDatabase().routes.indexOf(order.getRestaurantLocation());
            Location beforeIndex = Database.getInstanceDatabase().routes.get(index-1);
            Location afterIndex = Database.getInstanceDatabase().routes.get(index+1);
            Location thatIndex = Database.getInstanceDatabase().routes.get(index);
            if(rider1.equals(rider)){
                continue;
            } else if (rider1.getLocation().equals(beforeIndex) || rider1.getLocation().equals(afterIndex) || rider1.getLocation().equals(thatIndex)) {
                rider1.addNotification(new Notification(order));
            }
        }
        return RiderAcceptance.NOT_ACCEPTED;
    }

    @Override
    public Status receiveOrderFromRestaurant(int orderID) {
        return Database.getInstanceDatabase().setStatus(Status.PICKED, orderID);
    }

    @Override
    public Status deliverFoodToCustomer(int orderID) {
        Status status = Database.getInstanceDatabase().getStatus(orderID);
        if (!status.equals(Status.CANCELLED)) {
            return Database.getInstanceDatabase().setStatus(Status.DELIVERED, orderID);
        }
        return null;
    }

    @Override
    public Status getStatus(int orderID) {
        return Database.getInstanceDatabase().getStatus(orderID);
    }

    @Override
    public Status setStatusPrepared(int orderID) {
        return Database.getInstanceDatabase().setStatus(Status.PREPARED, orderID);
    }

}