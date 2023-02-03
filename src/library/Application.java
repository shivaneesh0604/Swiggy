package library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

final class Application implements CustomerApplication, RiderApplication, RestaurantManagerApplication {

    private final HashMap<String, HashMap<Integer, Order>> cartItems = new HashMap<>();//customerID->restaurantID,orderList
    Database database = Database.getInstanceDatabase();
    Application() {
    }

    @Override
    public HashMap<Integer, Restaurant> getAllRestaurant(Location location) {
        HashMap<Integer, Restaurant> listOfRestaurant = database.getAllRestaurant();
        HashMap<Integer, Restaurant> restaurants = new HashMap<>();
        Collection<Restaurant> restaurants1 = listOfRestaurant.values();
        ArrayList<Location> locations = database.getLocations();
        int index = locations.indexOf(location);
        Location previousIndex = index < 1 ? null : locations.get(index - 1);
        Location nextIndex = index > locations.size() - 2 ? null : locations.get(index + 1);
        for (Restaurant restaurant : restaurants1) {
            if (previousIndex == null) {
                if (restaurant.getRestaurantStatus().equals(RestaurantStatus.AVAILABLE) && (restaurant.getLocation().equals(location) || restaurant.getLocation().equals(nextIndex))) {
                    restaurants.put(restaurant.getRestaurantID(), restaurant);
                }
            } else if (nextIndex == null) {
                if ((restaurant.getRestaurantStatus().equals(RestaurantStatus.AVAILABLE) && (restaurant.getLocation().equals(location) || restaurant.getLocation().equals(previousIndex)))) {
                    restaurants.put(restaurant.getRestaurantID(), restaurant);
                }
            } else if (restaurant.getRestaurantStatus().equals(RestaurantStatus.AVAILABLE)) {
                restaurants.put(restaurant.getRestaurantID(), restaurant);
            }
        }
        return restaurants;
    }

    @Override
    public HashMap<String, Item> enterRestaurant(Restaurant restaurant, Timing timing) {
        return restaurant.getMenuList().getItems(timing);
    }

    @Override
    public String takeOrder(Item item, int quantity, String customerID, Restaurant restaurant) {
        HashMap<Integer, Order> customerOrder = cartItems.get(customerID);
        CustomerDetails customerDetails = database.getCustomerDetails(customerID);
        if (customerOrder != null) {
            Order restaurantOrder = customerOrder.get(restaurant.getRestaurantID());
            if (restaurantOrder != null) {
                restaurantOrder.addOrders(item, quantity);
            } else {
                Order order2 = new Order(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customerDetails.getLocation(), customerDetails.getUserID());
                order2.addOrders(item, quantity);
                customerOrder.put(restaurant.getRestaurantID(), order2);
            }
        } else {
            HashMap<Integer, Order> orderList2 = new HashMap<>();
            Order order = new Order(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customerDetails.getLocation(), customerDetails.getUserID());
            order.addOrders(item, quantity);
            orderList2.put(restaurant.getRestaurantID(), order);
            cartItems.put(customerID, orderList2);
        }
        return "order added with name " + item.getFoodName() + " with quantity " + quantity;
    }

    @Override
    public String removeOrder(Item item, int quantity, String customerID, Restaurant restaurant) {
        Order orders = cartItems.get(customerID).get(restaurant.getRestaurantID());
        if (orders != null) {
            return orders.deleteOrder(item, quantity);
        }
        return "no orders found in this restaurant for this customer";
    }

    @Override
    public HashMap<Integer, Order> viewItemsInCart(String customerID) {
        return cartItems.get(customerID);
    }

    @Override
    public Bill confirmOrder(String customerID, Restaurant restaurant) {
        Order order = cartItems.get(customerID).get(restaurant.getRestaurantID());
        if (order != null) {
            Bill bill = order.getBill();
            HashMap<String, LineOrder> orderInOrderList = order.getOrders();
            Collection<LineOrder> lineOrderCollection = orderInOrderList.values();
            for (LineOrder lineOrder : lineOrderCollection) {
                bill.addItem(lineOrder.getItem().getFoodName(), lineOrder.getQuantity(), lineOrder.getItem().getPrice());
            }
            return bill;
        }
        return null;
    }

    @Override
    public OrderStatus placeOrder(String customerID, Restaurant restaurant, Location location) {
        Order order2 = cartItems.get(customerID).get(restaurant.getRestaurantID());
        if (order2 != null) {
            database.addOrder(customerID, order2, restaurant.getRestaurantID());
            Collection<User> users1 = database.getAllUsers();
            ArrayList<Location> locations = database.getLocations();
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
            HashMap<String, LineOrder> orderInOrderList = order2.getOrders();
            ArrayList<LineOrder> lineOrderCollection = new ArrayList<>(orderInOrderList.values());
            restaurant.receiveOrders(order2.getOrderID(), lineOrderCollection);
            order2.setStatus(OrderStatus.PREPARING);
            return OrderStatus.PREPARING;
        }
        return null;
    }

    @Override
    public ArrayList<Order> viewOrdersPlaced(String customerID) {
        return database.getOrdersPlaced(customerID);
    }

    @Override
    public OrderStatus cancelOrder(int orderID) {
        Order order = database.getOrder(orderID);
        order.setStatus(OrderStatus.CANCELLED);
        ArrayList<Rider> riders = database.getAllRiders();
        for (Rider rider : riders) {
            ArrayList<Notification> notifications = rider.getNotification();
            for (Notification notification : notifications) {
                if (rider.getNotification().contains(notification)) {
                    rider.getNotification().remove(notification);
                }
            }
        }
        return OrderStatus.CANCELLED;
    }

    @Override
    public String checkStatusOfOrder(int orderID) {
        Order order = database.getOrder(orderID);
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
        Order order = database.getOrder(orderID);
        if (order.getOrderID() == orderID && !order.getStatus().equals(OrderStatus.CANCELLED)) {
            order.setRiderAcceptance(RiderFunctionalityStatus.ACCEPTED);
            return order.getRiderFunctionalityStatus();
        }
        return null;
    }

    @Override
    public RiderFunctionalityStatus declineOrder(Rider rider, Notification notification) {
        ArrayList<Rider> riders = database.getAllRiders();
        ArrayList<Location> locations = database.getLocations();
        notification.setCancelledRiderIds(rider.getUserID());
        int index = locations.indexOf(notification.getOrder().getRestaurantLocation());
        Location previousIndex = index < 1 ? null : locations.get(index - 1);
        Location nextIndex = index > locations.size() - 2 ? null : locations.get(index + 1);
        for (Rider rider1 : riders) {
            if (previousIndex == null) {
                if ((rider1.getLocation().equals(locations.get(index)) || rider1.getLocation().equals(nextIndex))
                        && !notification.checkCancelledRiderIds(rider1.getUserID())) {
                    rider1.addNotification(notification);
                    break;
                }
            } else if (nextIndex == null) {
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
        OrderStatus orderStatus = database.getStatus(orderID);
        if (!orderStatus.equals(OrderStatus.CANCELLED)) {
            return database.setStatusByRider(RiderFunctionalityStatus.PICKED, orderID);
        }
        return null;
    }

    @Override
    public RiderFunctionalityStatus changeStatusToDelivered(int orderID) {
        OrderStatus orderStatus = database.getStatus(orderID);
        if (!orderStatus.equals(OrderStatus.CANCELLED)) {
            return database.setStatusByRider(RiderFunctionalityStatus.DELIVERED, orderID);
        }
        return null;
    }

    @Override
    public OrderStatus setStatusPrepared(int orderID) {
        return database.setStatus(OrderStatus.PREPARED, orderID);
    }

}