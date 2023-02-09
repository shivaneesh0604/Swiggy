package library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


final class Database {

    private static Database database = null;
    private static final HashMap<UserCredential, User> users = new HashMap<>();
    private static final HashMap<Integer, Restaurant> listOfRestaurant = new HashMap<>();
    private static final HashMap<String, HashMap<Integer, ArrayList<Order>>> orders = new HashMap<>();// customerID->restaurantID,orderList
    static final ArrayList<Location> locations = new ArrayList<>();

    private Database() {
        Restaurant restaurant = new Restaurant(Location.AREA2, "anandha bhavan", 1);
        listOfRestaurant.put(1, restaurant);
        users.put(new UserCredential("shiva1234", "123456789"), new Customer("CUSTOMER_1001", ApplicationFactory.getCustomerApplication(), Role.CUSTOMER, "shiva"));
        Rider rider = new Rider("RIDER_1000", ApplicationFactory.getRiderApplication(), Role.RIDER, "sathya");
        rider.setLocation(Location.AREA1);
        users.put(new UserCredential("sathya1234", "123456789"), rider);
        users.put(new UserCredential("sankar1234", "123456789"), new RestaurantManager("RESTAURANT_MANAGER_1002", restaurant, ApplicationFactory.getRestaurantManagerApplication(), Role.RESTAURANT_MANAGER, "sankar"));
        Rider rider1 = new Rider("RIDER_1003", ApplicationFactory.getRiderApplication(), Role.RIDER, "durga_devi");
        rider1.setLocation(Location.AREA2);
        users.put(new UserCredential("dd1234", "123456789"), rider1);
        Rider rider2 = new Rider("RIDER_1004", ApplicationFactory.getRiderApplication(), Role.RIDER, "devi");
        rider2.setLocation(Location.AREA2);
        users.put(new UserCredential("devi1234", "123456789"), rider2);
        Item item = new Item("rice", 100, Dietary.VEG, Course.MAINCOURSE, Timing.AFTERNOON);
        Item item2 = new Item("chicken", 120, Dietary.NON_VEG, Course.MAINCOURSE, Timing.AFTERNOON);
        locations.add(Location.AREA1);
        locations.add(Location.AREA2);
        locations.add(Location.AREA3);
        locations.add(Location.AREA4);
        locations.add(Location.AREA5);
        restaurant.getMenuList().addMenusItems(item);
        restaurant.getMenuList().addMenusItems(item2);
    }

    ArrayList<Location> getLocations() {
        return locations;
    }

    static Database getInstanceDatabase() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    User addUser(User user, String userName, String passWord) {
        Collection<UserCredential> userCredentials = users.keySet();
        for (UserCredential userCredential : userCredentials) {
            if (userCredential.getUserName().equals(userName))
                return null;
        }
        users.put(new UserCredential(userName, passWord), user);
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

    HashMap<Integer, Restaurant> getAllRestaurant() {
        return new HashMap<>(listOfRestaurant);
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

    void addOrder(String customerID, Order tempOrders, int restaurantID) {
        HashMap<Integer, ArrayList<Order>> orderList1 = orders.get(customerID);
        if (orderList1 != null) {
            ArrayList<Order> order2 = orderList1.get(restaurantID);
            if (order2 != null) {
                order2.add(tempOrders);
            } else {
                orderList1.put(restaurantID, new ArrayList<>());
                ArrayList<Order> order3 = orderList1.get(restaurantID);
                order3.add(tempOrders);
            }
        } else {
            orders.put(customerID, new HashMap<>());
            HashMap<Integer, ArrayList<Order>> orders1 = orders.get(customerID);
            orders1.put(restaurantID, new ArrayList<>());
            ArrayList<Order> orders3 = orders1.get(restaurantID);
            orders3.add(tempOrders);
        }
    }

    HashMap<Integer, ArrayList<Order>> getOrdersPlaced(String customerID) {
        HashMap<Integer, ArrayList<Order>> orderPlaced = orders.get(customerID);
        if (orderPlaced != null) {
            return new HashMap<>(orderPlaced);
        }
        return null;
    }

    RiderFunctionalityStatus setStatusByRider(RiderFunctionalityStatus riderFunctionalityStatus, int orderID) {
        for (HashMap<Integer, ArrayList<Order>> innerMap : orders.values()) {
            for (ArrayList<Order> list : innerMap.values()) {
                for (Order order : list) {
                    if (order.getOrderID() == orderID) {
                        order.setRiderAcceptance(riderFunctionalityStatus);
                        return riderFunctionalityStatus;
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
        ArrayList<Rider> users2 = new ArrayList<>();
        for (User user : users1) {
            if (user.getRole().equals(Role.RIDER)) {
                users2.add((Rider) user);
            }
        }
        return users2;
    }

    Order getOrder(int orderID) {
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

}
