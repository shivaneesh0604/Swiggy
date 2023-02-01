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
    public String takeOrder(Item item, int quantity, String customerID, int restaurantID) {
        HashMap<Integer, Order> customerOrderList = cartItems.get(customerID);
        LineOrder lineOrder = new LineOrder(item, quantity);
        CustomerDetails customerDetails = Database.getInstanceDatabase().getCustomerDetails(customerID);
        Restaurant restaurant = Database.getInstanceDatabase().getRestaurant(restaurantID);
        if (customerOrderList != null) {
            Order restaurantOrder = customerOrderList.get(restaurantID);
            if (restaurantOrder.getRestaurantID() == restaurantID) {
                if (restaurantOrder != null) {
                    restaurantOrder.addOrders(item, quantity);
                } else {
                    Order order2 = new Order(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customerDetails.getLocation(), customerDetails.getUserID());
                    customerOrderList.put(restaurantID, order2);
                    order2.addOrders(item, quantity);
                }
            } else {
                customerOrderList.clear();
                Order order2 = new Order(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customerDetails.getLocation(), customerDetails.getUserID());
                customerOrderList.put(restaurantID, order2);
                order2.addOrders(item, quantity);
            }
        } else {
            HashMap<Integer, Order> orderList2 = new HashMap<>();
            Order order = new Order(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customerDetails.getLocation(), customerDetails.getUserID());
            order.addOrders(item, quantity);
            orderList2.put(restaurantID, order);
            cartItems.put(customerID, orderList2);
        }
        return "order added with name " + item.getFoodName() + " with quantity " + quantity;
    }

    @Override
    public String deleteOrder(Item item, int quantity, String customerID, int restaurantID) {
        Order orders = cartItems.get(customerID).get(restaurantID);
        if (orders != null) {
            return orders.deleteOrder(item, quantity);
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
                double price = Database.getInstanceDatabase().getPrice(lineOrder.getItem().getFoodName(), restaurantID);
                bill.addItem(lineOrder.getItem().getFoodName(), lineOrder.getQuantity(), price);
            }
            return bill;
        }
        return null;
    }

    @Override
    public OrderStatus placeOrder(String customerID, int restaurantID, Location location) {
        Order order2 = cartItems.get(customerID).get(restaurantID);
        if (order2 != null) {
            Database.getInstanceDatabase().addOrder(customerID, order2, restaurantID, location);
            Collection<User> users1 = Database.getInstanceDatabase().getAllUsers();
            for (User user : users1) {
                if (user.getRole().equals(Role.RIDER)) {
                    Rider rider = (Rider) user;
                    ArrayList<Location> locations = Database.getInstanceDatabase().getLocations();
                    int index = locations.indexOf(location);
                    if (rider.getLocation().equals(locations.get(index)) || rider.getLocation().equals(locations.get(index - 1)) || rider.getLocation().equals(locations.get(index + 1))) {
                        rider.addNotification(new Notification(order2));
                        break;
                    }
                }
            }
            cartItems.remove(customerID);
            Restaurant restaurant = Database.getInstanceDatabase().getRestaurant(restaurantID);
            HashMap<String, LineOrder> orderInOrderList = order2.getOrders();
            ArrayList<LineOrder> lineOrderCollection = new ArrayList<>(orderInOrderList.values());
            restaurant.receiveOrders(order2.getOrderID(), lineOrderCollection);
            return Database.getInstanceDatabase().setStatus(OrderStatus.PREPARING, order2.getOrderID());
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
    public OrderStatus cancelOrder(int orderID) {
        return Database.getInstanceDatabase().cancelOrder(OrderStatus.CANCELLED, orderID);
    }

    @Override
    public String checkStatusOfOrder(int orderID) {
        Order order = Database.getInstanceDatabase().getOrderlist(orderID);
        if (order != null) {
            if (!order.getStatus().equals(OrderStatus.CANCELLED)) {
                return "the status of food is " + order.getStatus() + " and the rider Acceptance status is " + order.getRiderFunctionalityStatus();
            } else {
                return "this order is cancelled";
            }
        }
        return "wrong orderID";
    }

    @Override
    public RiderFunctionalityStatus acceptOrder(int orderID) {
        Order order = Database.getInstanceDatabase().getOrderlist(orderID);
        if (order.getOrderID() == orderID && !order.getStatus().equals(OrderStatus.CANCELLED)) {
            order.setRiderAcceptance(RiderFunctionalityStatus.ACCEPTED);
            return order.getRiderFunctionalityStatus();
        }
        return null;
    }

    @Override
    public RiderFunctionalityStatus declineOrder(Order order, Rider rider, Notification notification) {
        ArrayList<Rider> riders = Database.getInstanceDatabase().getAllRiders();
        ArrayList<Location> locations = Database.getInstanceDatabase().getLocations();
        int index = locations.indexOf(order.getRestaurantLocation());
        notification.setCancelledRiderIds(rider.getUserID());
        for (Rider rider1 : riders) {
            if ((rider1.getLocation().equals(locations.get(index)) || rider1.getLocation().equals(locations.get(index + 1))
                    || rider1.getLocation().equals(locations.get(index - 1))) && !notification.checkCancelledRiderIds(rider1.getUserID())) {
                rider1.addNotification(notification);
                break;
            }
        }
        return RiderFunctionalityStatus.NOT_ACCEPTED;
    }

    @Override
    public RiderFunctionalityStatus changeStatusToPicked(int orderID) {
        return Database.getInstanceDatabase().setStatusByRider(RiderFunctionalityStatus.PICKED, orderID);
    }

    @Override
    public RiderFunctionalityStatus changeStatusToDelivered(int orderID) {
        OrderStatus orderStatus = Database.getInstanceDatabase().getStatus(orderID);
        if (!orderStatus.equals(OrderStatus.CANCELLED)) {
            return Database.getInstanceDatabase().setStatusByRider(RiderFunctionalityStatus.DELIVERED, orderID);
        }
        return null;
    }

    @Override
    public OrderStatus setStatusPrepared(int orderID) {
        return Database.getInstanceDatabase().setStatus(OrderStatus.PREPARED, orderID);
    }

}