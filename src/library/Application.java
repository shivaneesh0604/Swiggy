package library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

final class Application implements CustomerApplication, RiderApplication, RestaurantManagerApplication {

    private final HashMap<String, HashMap<Integer, Order>> cartItems = new HashMap<>();//customerID->restaurantID,orderList

    public Application() {
    }

    @Override
    public HashMap<String, Item> enterRestaurant(int restaurantID, Timing timing) {
        Restaurant restaurant = Database.getInstanceDatabase().getRestaurant(restaurantID);
        if (restaurant != null) {
            return restaurant.getMenuList().getItems(timing);
        }
        return null;
    }

    @Override
    public HashMap<Integer, String> getAllRestaurant(Location location) {
        return Database.getInstanceDatabase().getAllRestaurant(location);//todo: get all restaurant and process here
    }

    @Override
    public String takeOrder(Item item, int quantity, String customerID, int restaurantID) {
        HashMap<Integer, Order> customerOrder = cartItems.get(customerID);
        CustomerDetails customerDetails = Database.getInstanceDatabase().getCustomerDetails(customerID);
        Restaurant restaurant = Database.getInstanceDatabase().getRestaurant(restaurantID);
        if (customerOrder != null) {
            Order restaurantOrder = customerOrder.get(restaurantID);
            if (restaurantOrder.getRestaurantID() == restaurantID) {
                if (restaurantOrder != null) {
                    restaurantOrder.addOrders(item, quantity);
                } else {
                    Order order2 = new Order(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customerDetails.getLocation(), customerDetails.getUserID());
                    customerOrder.put(restaurantID, order2);
                    order2.addOrders(item, quantity);
                }
            } else {
                customerOrder.clear();
                Order order2 = new Order(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customerDetails.getLocation(), customerDetails.getUserID());
                customerOrder.put(restaurantID, order2);
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
    public String removeOrder(Item item, int quantity, String customerID, int restaurantID) {
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
            ArrayList<Location> locations = Database.getInstanceDatabase().getLocations();
            int index = locations.indexOf(location);
            Location previousIndex = index < 1 ? null : locations.get(index - 1);
            Location nextIndex = index > locations.size() - 2 ? null : locations.get(index + 1);
            for (User user : users1) {
                if (user.getRole().equals(Role.RIDER)) {
                    Rider rider = (Rider) user;
                    if (previousIndex == null) {
                        if (rider.getLocation().equals(location) || rider.getLocation().equals(nextIndex)) {
                            rider.addNotification(new Notification(order2));
                            break;
                        }
                    } else if (nextIndex == null) {
                        if (rider.getLocation().equals(location) || rider.getLocation().equals(previousIndex)) {
                            rider.addNotification(new Notification(order2));
                        }
                    } else {
                        rider.addNotification(new Notification(order2));
                    }
                }
            }
            cartItems.remove(customerID);
            Restaurant restaurant = Database.getInstanceDatabase().getRestaurant(restaurantID);
            HashMap<String, LineOrder> orderInOrderList = order2.getOrders();
            ArrayList<LineOrder> lineOrderCollection = new ArrayList<>(orderInOrderList.values());
            restaurant.receiveOrders(order2.getOrderID(), lineOrderCollection);
            order2.setStatus(OrderStatus.PREPARED);
            return OrderStatus.PREPARING;
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
        notification.setCancelledRiderIds(rider.getUserID());
        int index = locations.indexOf(order.getRestaurantLocation());
        Location previousIndex = index < 1 ? null : locations.get(index - 1);
        Location nextIndex = index > locations.size() - 2 ? null : locations.get(index + 1);
        for (Rider rider1 : riders) {
            if (previousIndex == null) {
                if ((rider1.getLocation().equals(locations.get(index)) || rider1.getLocation().equals(nextIndex))
                         && !notification.checkCancelledRiderIds(rider1.getUserID())) {
                    rider1.addNotification(notification);
                    break;
                }
            } else if (nextIndex==null) {
                if ((rider1.getLocation().equals(locations.get(index)) || rider1.getLocation().equals(previousIndex))
                        && !notification.checkCancelledRiderIds(rider1.getUserID())) {
                    rider1.addNotification(notification);
                    break;
                }
            } else if ((rider1.getLocation().equals(locations.get(index)) || rider1.getLocation().equals(nextIndex) || rider1.getLocation().equals(previousIndex))
                    && !notification.checkCancelledRiderIds(rider1.getUserID())) {
                rider1.addNotification(notification);
                break;
            }
        }
        return RiderFunctionalityStatus.NOT_ACCEPTED;
    }

    @Override
    public RiderFunctionalityStatus changeStatusToPicked(int orderID) {
        OrderStatus orderStatus = Database.getInstanceDatabase().getStatus(orderID);
        if (!orderStatus.equals(OrderStatus.CANCELLED)) {
            return Database.getInstanceDatabase().setStatusByRider(RiderFunctionalityStatus.PICKED, orderID);
        }
        return null;
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