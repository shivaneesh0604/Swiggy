package library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

final class Application implements CustomerApplication, RiderApplication, RestaurantManagerApplication {

    private final HashMap<String, HashMap<Integer, Order>> cartItems = new HashMap<>();//customerID->restaurantID,orderList
    private static final ArrayList<Notification> tempNotifications = new ArrayList<>();

    Application() {
    }

    @Override
    public HashMap<Integer, Restaurant> getAllRestaurant(Location location) {
        HashMap<Integer, Restaurant> listOfRestaurant = Database.getInstanceDatabase().getAllRestaurant();
        HashMap<Integer, Restaurant> restaurants = new HashMap<>();
        ArrayList<Location> locations = Database.getInstanceDatabase().getLocations();
        int index = locations.indexOf(location);
        Location previousIndex = index < 1 ? null : locations.get(index - 1);
        Location nextIndex = index > locations.size() - 2 ? null : locations.get(index + 1);
        for (Restaurant restaurant : listOfRestaurant.values()) {
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
                restaurantOrder.addOrders(item);
            } else {
                customerOrder.clear();
                Order order2 = new Order(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customerDetails.getUserID(), customerDetails.getLocation());
                order2.addOrders(item);
                customerOrder.put(restaurant.getRestaurantID(), order2);
            }
        } else {
            HashMap<Integer, Order> orderList2 = new HashMap<>();
            Order order = new Order(restaurant.getRestaurantName(), restaurant.getRestaurantID(), restaurant.getLocation(), customerDetails.getUserID(), customerDetails.getLocation());
            order.addOrders(item);
            orderList2.put(restaurant.getRestaurantID(), order);
            cartItems.put(customerID, orderList2);
        }
        return OrderAddition.ORDER_ADDED;
    }

    @Override
    public OrderDeletion removeOrder(Item item, String customerID, Restaurant restaurant) {
        Order orders = cartItems.get(customerID).get(restaurant.getRestaurantID());
        if (orders != null) {
            OrderDeletion orderDeletion = orders.deleteOrder(item);
            if (orderDeletion.equals(OrderDeletion.TOTALLY_DELETED)) {
                cartItems.get(customerID).clear();
                System.out.println(cartItems.size());
                return orderDeletion;
            }
        }
        return OrderDeletion.NO_ORDER_FOUND_IN_THIS_RESTAURANT;
    }

    @Override
    public HashMap<Integer, Order> viewItemsInCart(String customerID) {
        if (cartItems.get(customerID) == null) {
            return null;
        }
        return new HashMap<>(cartItems.get(customerID));
    }

    @Override
    public Bill confirmOrder(String customerID, Restaurant restaurant) {
        Order order = cartItems.get(customerID).get(restaurant.getRestaurantID());
        if (order != null) {
            Bill bill = order.getBill();
            HashMap<String, LineOrder> orders = order.getOrders();
            Collection<LineOrder> lineOrders = orders.values();
            for (LineOrder lineOrder : lineOrders) {
                bill.addItem(lineOrder.getItem().getFoodName(), lineOrder.getQuantity(), lineOrder.getItem().getPrice());
            }
            return bill;
        }
        return null;
    }

    @Override
    public OrderStatus placeOrder(String customerID, Restaurant restaurant) {
        Order order = cartItems.containsKey(customerID) ? (cartItems.get(customerID).getOrDefault(restaurant.getRestaurantID(), null)) : null;
        if (order != null) {
            Database.getInstanceDatabase().addOrder(customerID, order, restaurant.getRestaurantID());
            Collection<Rider> allRiders = Database.getInstanceDatabase().getAllRiders();
            setNotification(allRiders, order.getRestaurantLocation(), new Notification(order));
            cartItems.remove(customerID);
            HashMap<String, LineOrder> orders = order.getOrders();
            ArrayList<LineOrder> lineOrders = new ArrayList<>(orders.values());
            restaurant.receiveOrders(order.getOrderID(), lineOrders);
            order.setStatus(OrderStatus.ORDER_PLACED);
            return OrderStatus.ORDER_PLACED;
        }
        return null;
    }

    @Override
    public ArrayList<Order> viewOrdersPlaced(String customerID) {
        ArrayList<Order> ordersPlaced = new ArrayList<>();
        HashMap<Integer, ArrayList<Order>> ordersPlacedByCustomer = Database.getInstanceDatabase().getOrdersPlaced(customerID);
        if (ordersPlacedByCustomer != null) {
            HashMap<Integer, ArrayList<Order>> nonCancelledOrders = getNonCancelledOrders(ordersPlacedByCustomer);
            if (nonCancelledOrders.size() > 0) {
                for (ArrayList<Order> orders1 : nonCancelledOrders.values()) {
                    for (Order order : orders1) {
                        if (!order.getStatus().equals(OrderStatus.CANCELLED)) {
                            ordersPlaced.add(order);
                        }
                    }
                }
                return ordersPlaced;
            }
        }
        return null;
    }

    @Override
    public OrderStatus cancelOrder(int orderID) {
        Order order = Database.getInstanceDatabase().getOrder(orderID);
        if (order != null) {
            order.setStatus(OrderStatus.CANCELLED);
            ArrayList<Rider> riders = Database.getInstanceDatabase().getAllRiders();
            for (Rider rider : riders) {
                rider.removeNotification(orderID);
            }
            return OrderStatus.CANCELLED;
        }
        return null;
    }

    // rider starts//

    @Override
    public void setNotification(Rider rider) {
        ArrayList<Rider> riders = Database.getInstanceDatabase().getAllRiders();
        if (rider.getRiderStatus().equals(RiderStatus.AVAILABLE)) {
            System.out.println(tempNotifications);
            for (Notification notification : tempNotifications) {
                setNotification(riders, notification.getOrder().getRestaurantLocation(), notification);
            }
            tempNotifications.clear();
            return;
        }
        riders.remove(rider);
        for (Notification notification : rider.getNotification()) {
            setNotification(riders, notification.getOrder().getRestaurantLocation(), notification);
            rider.removeNotification(notification.getOrder().getOrderID());
        }
    }

    @Override
    public RiderFunctionalityStatus acceptOrder(Rider rider, int orderID) {
        Order order = Database.getInstanceDatabase().getOrder(orderID);
        if (order != null && order.getOrderID() == orderID && !order.getStatus().equals(OrderStatus.CANCELLED)) {
            order.setRiderAcceptance(RiderFunctionalityStatus.ACCEPTED);
            order.setRiderName(rider.getName());
            Collection<Rider> allRiders = Database.getInstanceDatabase().getAllRiders();
            allRiders.remove(rider);
            rider.removeNotification(orderID);
            ArrayList<Notification> notifications = rider.getNotification();
            for (Notification notification1 : notifications) {
                setNotification(allRiders, order.getRestaurantLocation(), notification1);
            }
            return order.getRiderFunctionalityStatus();
        }
        return null;
    }

    @Override
    public RiderFunctionalityStatus declineOrder(Rider rider, Notification notification) {
        ArrayList<Rider> riders = Database.getInstanceDatabase().getAllRiders();
        notification.setCancelledRiderIds(rider.getUserID());
        rider.removeNotification(notification.getOrder().getOrderID());
        setNotification(riders, notification.getOrder().getRestaurantLocation(), notification);
        return RiderFunctionalityStatus.NOT_ACCEPTED;
    }

    @Override
    public void changeStatusByRider(Order order, RiderFunctionalityStatus riderFunctionalityStatus) {
        Database.getInstanceDatabase().setStatusByRider(riderFunctionalityStatus, order.getOrderID());
    }

    @Override
    public OrderStatus setStatusPrepared(int orderID, OrderStatus orderStatus) {
        return Database.getInstanceDatabase().setStatus(orderStatus, orderID);
    }

    private HashMap<Integer, ArrayList<Order>> getNonCancelledOrders(HashMap<Integer, ArrayList<Order>> ordersPlacedByCustomer) {
        HashMap<Integer, ArrayList<Order>> ordersPlaced = new HashMap<>();
        for (ArrayList<Order> orders1 : ordersPlacedByCustomer.values()) {
            for (Order order : orders1) {
                if (!order.getStatus().equals(OrderStatus.CANCELLED)) {
                    if (ordersPlaced.containsKey(order.getOrderID())) {
                        ordersPlaced.get(order.getOrderID()).add(order);
                    } else {
                        ordersPlaced.put(order.getOrderID(), new ArrayList<>());
                        ordersPlaced.get(order.getOrderID()).add(order);
                    }
                }
            }
        }
        return ordersPlaced;
    }

    private void setNotification(Collection<Rider> riders, Location location, Notification notification) {
        ArrayList<Location> locations = Database.getInstanceDatabase().getLocations();
        int index = locations.indexOf(location);
        Location previousIndex = index < 1 ? null : locations.get(index - 1);
        Location nextIndex = index > locations.size() - 2 ? null : locations.get(index + 1);
        for (Rider rider : riders) {
            if (previousIndex == null) {
                if ((rider.getLocation().equals(location) || rider.getLocation().equals(nextIndex)) && notification.checkCancelledRiderIds(rider.getUserID()) && rider.getRiderStatus().equals(RiderStatus.AVAILABLE)) {
                    System.out.println(rider.getName() +" is the rider");
                    rider.addNotification(notification);
                    return;
                }
            } else if (nextIndex == null) {
                if ((rider.getLocation().equals(location) || rider.getLocation().equals(previousIndex)) && notification.checkCancelledRiderIds(rider.getUserID()) && rider.getRiderStatus().equals(RiderStatus.AVAILABLE)) {
                    System.out.println(rider.getName()+" is the rider");
                    rider.addNotification(notification);
                    return;
                }
            } else if (notification.checkCancelledRiderIds(rider.getUserID()) && rider.getRiderStatus().equals(RiderStatus.AVAILABLE) && ((rider.getLocation().equals(location) || rider.getLocation().equals(previousIndex) || rider.getLocation().equals(nextIndex)))) {
                rider.addNotification(notification);
                System.out.println(rider.getName()+" is the rider");
                return;
            }
        }
        tempNotifications.add(notification);
    }
}