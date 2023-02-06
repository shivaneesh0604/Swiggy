package library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

final class Application implements CustomerApplication, RiderApplication, RestaurantManagerApplication {

    private final HashMap<String, HashMap<Integer, Order>> cartItems = new HashMap<>();//customerID->restaurantID,orderList

    Application() {
    }

    @Override
    public HashMap<Integer, Restaurant> getAllRestaurant(Location location) {
        HashMap<Integer, Restaurant> listOfRestaurant = Database.getInstanceDatabase().getAllRestaurant();
        HashMap<Integer, Restaurant> restaurants = new HashMap<>();
        Collection<Restaurant> restaurants1 = listOfRestaurant.values();
        ArrayList<Location> locations = Database.getInstanceDatabase().getLocations();
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
            } else if (restaurant.getRestaurantStatus().equals(RestaurantStatus.AVAILABLE) && ((restaurant.getLocation().equals(location)) || restaurant.getLocation().equals(previousIndex)) || restaurant.getLocation().equals(nextIndex)) {
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
    public OrderAddition takeOrder(Item item, String customerID, Restaurant restaurant) {
        HashMap<Integer, Order> customerOrder = cartItems.get(customerID);
        CustomerDetails customerDetails = Database.getInstanceDatabase().getCustomerDetails(customerID);
        if (customerOrder != null) {
            Order restaurantOrder = customerOrder.get(restaurant.getRestaurantID());
            if (restaurantOrder != null) {
                restaurantOrder.addOrders(item, 1);
            } else {
                Order order2 = new Order(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customerDetails.getLocation(), customerDetails.getUserID());
                order2.addOrders(item, 1);
                customerOrder.put(restaurant.getRestaurantID(), order2);
            }
        } else {
            HashMap<Integer, Order> orderList2 = new HashMap<>();
            Order order = new Order(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customerDetails.getLocation(), customerDetails.getUserID());
            order.addOrders(item, 1);
            orderList2.put(restaurant.getRestaurantID(), order);
            cartItems.put(customerID, orderList2);
        }
        return OrderAddition.ORDER_ADDED;
    }

    @Override
    public OrderDeletion removeOrder(Item item, String customerID, Restaurant restaurant) {
        Order orders = cartItems.get(customerID).get(restaurant.getRestaurantID());
        if (orders != null) {
            return orders.deleteOrder(item, 1);
        }
        return OrderDeletion.NO_ORDER_FOUND_IN_THIS_RESTAURANT;
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
            Database.getInstanceDatabase().addOrder(customerID, order2, restaurant.getRestaurantID());
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
                            break;
                        }
                    } else {
                        rider.addNotification(new Notification(order2));
                        break;
                    }
                }
            }
            cartItems.remove(customerID);
            HashMap<String, LineOrder> orderInOrderList = order2.getOrders();
            ArrayList<LineOrder> lineOrderCollection = new ArrayList<>(orderInOrderList.values());
            restaurant.receiveOrders(order2.getOrderID(), lineOrderCollection);
            order2.setStatus(OrderStatus.PLACED);
            return OrderStatus.PLACED;
        }
        return null;
    }

    @Override
    public HashMap<Integer, ArrayList<Order>> viewOrdersPlaced(String customerID) {
        return Database.getInstanceDatabase().getOrdersPlaced(customerID);
    }

    @Override
    public OrderStatus cancelOrder(int orderID) {
        Order order = Database.getInstanceDatabase().getOrder(orderID);
        order.setStatus(OrderStatus.CANCELLED);
        ArrayList<Rider> riders = Database.getInstanceDatabase().getAllRiders();
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
        Order order = Database.getInstanceDatabase().getOrder(orderID);
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
        Order order = Database.getInstanceDatabase().getOrder(orderID);
        if (order.getOrderID() == orderID && !order.getStatus().equals(OrderStatus.CANCELLED)) {
            order.setRiderAcceptance(RiderFunctionalityStatus.ACCEPTED);
            return order.getRiderFunctionalityStatus();
        }
        return null;
    }

    @Override
    public RiderFunctionalityStatus declineOrder(Rider rider, Notification notification) {
        ArrayList<Rider> riders = Database.getInstanceDatabase().getAllRiders();
        ArrayList<Location> locations = Database.getInstanceDatabase().getLocations();
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