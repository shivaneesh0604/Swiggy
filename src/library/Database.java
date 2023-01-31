package library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


final class Database {

    private static Database database = null;
    private static final HashMap<String, User> users = new HashMap<String, User>();
    private static final HashMap<Integer, Restaurant> listOfRestaurant = new HashMap<Integer, Restaurant>();
    private static final HashMap<String, HashMap<Integer, ArrayList<OrderList>>> orders = new HashMap<>();// customerID->restaurantID,orderList

    private Database() {
        Restaurant restaurant = new Restaurant("adayar", "anandha bhavan", 1);
        listOfRestaurant.put(1, restaurant);
        users.put("shiva1234", new Customer("shiva1234", "123456789",ApplicationFactory.getCustomerApplication() , Role.CUSTOMER, "shiva"));
        users.put("sathya1234", new Rider("sathya1234", "123456789", ApplicationFactory.getRiderApplication() ,Role.RIDER, "sathya"));
        users.put("sankar1234", new RestaurantManager("sankar1234", "123456789", restaurant,ApplicationFactory.getRestaurantManagerApplication() , Role.RESTAURANT_MANAGER, "sankar"));
    }

    static Database getInstance() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }
    User addUser(User user) {
        this.users.put(user.getUserID(), user);
        return user;
    }

    User getUser(String userName) {
        if (users.containsKey(userName)) {
            User user = users.get(userName);
            return user;
        }
        return null;
    }

    Restaurant getRestaurant(int restaurantID) {
        if (listOfRestaurant.containsKey(restaurantID)) {
            return listOfRestaurant.get(restaurantID);
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

    Status setStatus(Status status, int orderID) {
        for (HashMap<Integer, ArrayList<OrderList>> innerMap : orders.values()) {
            for (ArrayList<OrderList> list : innerMap.values()) {
                for (OrderList orderList : list) {
                    if (orderList.getOrderID() == orderID) {
                        orderList.setStatus(status);
                        return status;
                    }
                }
            }
        }
        return null;
    }

    Status getStatus(int orderID) {
        for (HashMap<Integer, ArrayList<OrderList>> innerMap : orders.values()) {
            for (ArrayList<OrderList> list : innerMap.values()) {
                for (OrderList orderList : list) {
                    if (orderList.getOrderID() == orderID) {
                        return orderList.getStatus();
                    }
                }
            }
        }
        return null;
    }

    void addOrder(String customerID, OrderList tempOrders, int restaurantID) {
        HashMap<Integer, ArrayList<OrderList>> orderList1 = orders.get(customerID);
        if (orderList1 != null) {
            ArrayList<OrderList> orderList2 = orderList1.get(restaurantID);
            if (orderList2 != null) {
                orderList2.add(tempOrders);
            } else {
                orderList1.put(restaurantID, new ArrayList<OrderList>());
                ArrayList<OrderList> orderList3 = orderList1.get(restaurantID);
                orderList3.add(tempOrders);
            }
        } else {
            orders.put(customerID, new HashMap<Integer, ArrayList<OrderList>>());
            HashMap<Integer, ArrayList<OrderList>> orders1 = orders.get(customerID);
            orders1.put(restaurantID, new ArrayList<OrderList>());
            ArrayList<OrderList> orders3 = orders1.get(restaurantID);
            orders3.add(tempOrders);
        }
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

    ArrayList<OrderList> getOrders(String customerID) {
        Collection<OrderList> collection = new ArrayList<>();
        for (HashMap<Integer, ArrayList<OrderList>> innerMap : orders.values()) {
            for (ArrayList<OrderList> list : innerMap.values()) {
                for (OrderList orderList : list) {
                    if (orderList.getCustomerID().equals(customerID) && !orderList.getStatus().equals(Status.CANCELLED) && !orderList.getStatus().equals(Status.DELIVERED)) {
                        collection.add(orderList);
                    }
                }
            }
        }
        return (ArrayList<OrderList>) collection;
    }

    ArrayList<OrderList> getAllOrders() {
        Collection<OrderList> collection = new ArrayList<>();
        for (HashMap<Integer, ArrayList<OrderList>> innerMap : orders.values()) {
            for (ArrayList<OrderList> list : innerMap.values()) {
                for (OrderList orderList : list) {
                    if (orderList.getRiderAcceptance().equals(RiderAcceptance.NOT_ACCEPTED))
                        collection.add(orderList);
                }
            }
        }
        return (ArrayList<OrderList>) collection;
    }

    OrderList getOrderlist(int orderID){
        for (HashMap<Integer, ArrayList<OrderList>> innerMap : orders.values()) {
            for (ArrayList<OrderList> list : innerMap.values()) {
                for (OrderList orderList : list) {
                    if(orderList.getOrderID()==orderID){
                        return orderList;
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
