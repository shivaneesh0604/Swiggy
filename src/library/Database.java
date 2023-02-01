package library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


final class Database {

    private static Database database = null;
    private static final HashMap<UserCredential, User> users = new HashMap<>();
    private static final HashMap<Integer, Restaurant> listOfRestaurant = new HashMap<Integer, Restaurant>();
    private static final HashMap<String, HashMap<Integer, ArrayList<Order>>> orders = new HashMap<>();// customerID->restaurantID,orderList
    static final ArrayList<Location> routes = new ArrayList<Location>();

    private Database() {
        Restaurant restaurant = new Restaurant(Location.AREA1, "anandha bhavan", 1);
        listOfRestaurant.put(1, restaurant);
        users.put(new UserCredential("shiva1234", "123456789", "CUSTOMER_1001"), new Customer("CUSTOMER_1001", ApplicationFactory.getCustomerApplication(), Role.CUSTOMER, "shiva"));
        users.put(new UserCredential("sathya1234", "123456789", "RIDER_1000"), new Rider("RIDER_1000", ApplicationFactory.getRiderApplication(), Role.RIDER, "sathya"));
        users.put(new UserCredential("sankar1234", "123456789", "RESTAURANT_MANAGER_1002"), new RestaurantManager("RESTAURANT_MANAGER_1002", restaurant, ApplicationFactory.getRestaurantManagerApplication(), Role.RESTAURANT_MANAGER, "sankar"));
        users.put(new UserCredential("dd1234", "123456789", "RIDER_1003"), new Rider("RIDER_1003", ApplicationFactory.getRiderApplication(), Role.RIDER, "devi"));
        Item item = new Item("rice", 100, Dietary.VEG, Course.MAINCOURSE, Timing.AFTERNOON);
        Item item2 = new Item("chicken", 120, Dietary.NON_VEG, Course.MAINCOURSE, Timing.AFTERNOON);
        routes.add(Location.AREA1);
        routes.add(Location.AREA2);
        routes.add(Location.AREA3);
        routes.add(Location.AREA4);
        routes.add(Location.AREA5);
        restaurant.getMenuList().addMenusItems(item);
        restaurant.getMenuList().addMenusItems(item2);
    }

    public static Database getInstanceDatabase() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    User addUser(User user, String userName, String passWord) {
        this.users.put(new UserCredential(userName, passWord, user.getUserID()), user);
        return user;
    }

    User getUser(String userName, String passWord) {
        Collection<UserCredential> userCredentials = users.keySet();
        for (UserCredential userCredential : userCredentials) {
            if (userCredential.getUserName().equals(userName)) {
                if (userCredential.getPassWord().equals(passWord)) {
                    return users.get(userCredential);
                }
            }
        }
        return null;
    }

    HashMap<Integer, String> getAllRestaurant() {
        HashMap<Integer, String> restaurants = new HashMap<>();
        Collection<Restaurant> restaurants1 = listOfRestaurant.values();
        for (Restaurant restaurant : restaurants1) {
            if (restaurant.getRestaurantStatus().equals(RestaurantStatus.AVAILABLE))
                restaurants.put(restaurant.getRestaurantID(), restaurant.getRestaurantName());
        }
        return restaurants;
    }

    CustomerDetails getCustomerDetails(String customerID) {
        Collection<User> users1 = users.values();
        for (User user : users1) {
            if (user.getUserID().equals(customerID)) {
                Customer customer = (Customer) user;
                return new CustomerDetails(customer.getLocation(), customer.getUserID());
            }
        }
        return null;
    }

    Restaurant getRestaurant(int restaurantID) {
        if (listOfRestaurant.containsKey(restaurantID)) {
            return listOfRestaurant.get(restaurantID);
        }
        return null;
    }

    void addOrder(String customerID, Order tempOrders, int restaurantID, Location location) {
        HashMap<Integer, ArrayList<Order>> orderList1 = orders.get(customerID);
        if (orderList1 != null) {
            ArrayList<Order> order2 = orderList1.get(restaurantID);
            if (order2 != null) {
                order2.add(tempOrders);
            } else {
                orderList1.put(restaurantID, new ArrayList<Order>());
                ArrayList<Order> order3 = orderList1.get(restaurantID);
                order3.add(tempOrders);
            }
        } else {
            orders.put(customerID, new HashMap<Integer, ArrayList<Order>>());
            HashMap<Integer, ArrayList<Order>> orders1 = orders.get(customerID);
            orders1.put(restaurantID, new ArrayList<Order>());
            ArrayList<Order> orders3 = orders1.get(restaurantID);
            orders3.add(tempOrders);
        }
        Collection<User> users1 = users.values();
        for (User user : users1) {
            if (user.getRole().equals(Role.RIDER)) {
                Rider rider = (Rider) user;
                int index = routes.indexOf(location);
                Location beforeIndex = routes.get(index - 1);
                Location afterIndex = routes.get(index + 1);
                Location thatIndex = routes.get(index);
                if (rider.getLocation().equals(thatIndex) || rider.getLocation().equals(beforeIndex) || rider.getLocation().equals(afterIndex)) {
                    rider.addNotification(new Notification(tempOrders));
                    break;
                }
            }
        }
    }

    OrderStatus cancelOrder(OrderStatus orderStatus, int orderID) {
        for (HashMap<Integer, ArrayList<Order>> innerMap : orders.values()) {
            for (ArrayList<Order> list : innerMap.values()) {
                for (Order order : list) {
                    if (order.getOrderID() == orderID) {
                        order.setStatus(orderStatus);
                        ArrayList<Rider> riders = getAllRiders();
                        for (Rider rider : riders) {
                            ArrayList<Notification> notifications = rider.getNotification();
                            for (Notification notification : notifications) {
                                if (rider.getNotification().contains(notification)) {
                                    rider.getNotification().remove(notification);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    ArrayList<Order> getOrders(String customerID) {
        Collection<Order> collection = new ArrayList<>();
        for (HashMap<Integer, ArrayList<Order>> innerMap : orders.values()) {
            for (ArrayList<Order> list : innerMap.values()) {
                for (Order order : list) {
                    if (order.getCustomerID().equals(customerID) && !order.getStatus().equals(OrderStatus.CANCELLED) && !order.getRiderAcceptance().equals(RiderAcceptance.DELIVERED)) {
                        collection.add(order);
                    }
                }
            }
        }
        return (ArrayList<Order>) collection;
    }

    RiderAcceptance setStatusByRider(RiderAcceptance riderAcceptance, int orderID) {
        for (HashMap<Integer, ArrayList<Order>> innerMap : orders.values()) {
            for (ArrayList<Order> list : innerMap.values()) {
                for (Order order : list) {
                    if (order.getOrderID() == orderID) {
                        order.setRiderAcceptance(riderAcceptance);
                        return riderAcceptance;
                    }
                }
            }
        }
        return null;
    }

    OrderStatus setStatus(OrderStatus orderStatus, int orderID) {
        for (HashMap<Integer, ArrayList<Order>> innerMap : orders.values()) {
            for (ArrayList<Order> list : innerMap.values()) {
                for (Order order : list) {
                    if (order.getOrderID() == orderID) {
                        order.setStatus(orderStatus);
                        return orderStatus;
                    }
                }
            }
        }
        return null;
    }

    ArrayList<Rider> getAllRiders() {
        Collection<User> users1 = users.values();
        Collection<Rider> users2 = new ArrayList<>();
        for (User user : users1) {
            if (user.getRole().equals(Role.RIDER)) {
                users2.add((Rider) user);
            }
        }
        return (ArrayList<Rider>) users2;
    }

    OrderStatus getStatus(int orderID) {
        for (HashMap<Integer, ArrayList<Order>> innerMap : orders.values()) {
            for (ArrayList<Order> list : innerMap.values()) {
                for (Order order : list) {
                    if (order.getOrderID() == orderID) {
                        return order.getStatus();
                    }
                }
            }
        }
        return null;
    }


//    public Restaurant findRestaurant(int orderID){
//        for (HashMap<Integer, ArrayList<OrderList>> innerMap : orders.values()) {
//            for (ArrayList<OrderList> list : innerMap.values()) {
//                for (OrderList orderList : list) {
//                    if(orderList.getOrderID()==orderID){
//                        return getRestaurant(orderList.getRestaurantID());
//                    }
//                }
//            }
//        }
//        return null;
//    }

//    ArrayList<OrderList> getAllOrders() {
//        Collection<OrderList> collection = new ArrayList<>();
//        for (HashMap<Integer, ArrayList<OrderList>> innerMap : orders.values()) {
//            for (ArrayList<OrderList> list : innerMap.values()) {
//                for (OrderList orderList : list) {
//                    if (orderList.getRiderAcceptance().equals(RiderAcceptance.NOT_ACCEPTED))
//                        collection.add(orderList);
//                }
//            }
//        }
//        return (ArrayList<OrderList>) collection;
//    }

    Order getOrderlist(int orderID) {
        for (HashMap<Integer, ArrayList<Order>> innerMap : orders.values()) {
            for (ArrayList<Order> list : innerMap.values()) {
                for (Order order : list) {
                    if (order.getOrderID() == orderID) {
                        return order;
                    }
                }
            }
        }
        return null;
    }

    double getPrice(String foodName, int restaurantID) {
        MenuList menuList = listOfRestaurant.get(restaurantID).getMenuList();
        return menuList.getPrice(foodName);
    }

}
