package library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


final class Database {

    private static Database database = null;
    private static final HashMap<UserCredential,User> users = new HashMap<>();
    private static final HashMap<Integer, Restaurant> listOfRestaurant = new HashMap<Integer, Restaurant>();
    private static final HashMap<String, HashMap<Integer, ArrayList<OrderList>>> orders = new HashMap<>();// customerID->restaurantID,orderList
    private static final ArrayList<Location> routes = new ArrayList<Location>();
    private Database() {
        Restaurant restaurant = new Restaurant(Location.AREA1, "anandha bhavan", 1);
        listOfRestaurant.put(1, restaurant);
        users.put(new UserCredential("shiva1234","123456789","CUSTOMER_1001"),new Customer("CUSTOMER_1001",ApplicationFactory.getCustomerApplication() , Role.CUSTOMER, "shiva"));
        users.put(new UserCredential("sathya1234","123456789","RIDER_1000"),new Rider("RIDER_1000", ApplicationFactory.getRiderApplication() ,Role.RIDER, "sathya"));
        users.put(new UserCredential("sankar1234","123456789","RESTAURANT_MANAGER_1002"),new RestaurantManager("RESTAURANT_MANAGER_1002",  restaurant,ApplicationFactory.getRestaurantManagerApplication() , Role.RESTAURANT_MANAGER, "sankar"));
        users.put(new UserCredential("dd1234","123456789","RIDER_1003"),new Rider("RIDER_1003", ApplicationFactory.getRiderApplication() ,Role.RIDER, "devi"));
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
        this.users.put(new UserCredential(userName,passWord,user.getUserID()),user);
        return user;
    }

    User getUser(String userName,String passWord) {
        Collection<UserCredential> userCredentials = users.keySet();
        for(UserCredential userCredential:userCredentials){
            if(userCredential.getUserName().equals(userName)){
                if(userCredential.getPassWord().equals(passWord)){
                    return users.get(userCredential);
                }
            }
        }
        return null;
    }

    CustomerDetails getCustomerDetails(String customerID){
        Collection<User> users1 = users.values();
        for(User user:users1){
            if(user.getUserID().equals(customerID)){
                Customer customer = (Customer) user;
                return new CustomerDetails(customer.getLocation(),customer.getUserID());
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

    void addOrder(String customerID, OrderList tempOrders, int restaurantID,Location location) {
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
        Collection<User> users1 = users.values();
        for(User user:users1){
            if(user.getRole().equals(Role.RIDER)){
                Rider rider = (Rider) user;
                int index = routes.indexOf(location);
                Location beforeIndex = routes.get(index-1);
                Location afterIndex = routes.get(index+1);
                Location thatIndex = routes.get(index);
                if(rider.getLocation().equals(thatIndex) || rider.getLocation().equals(beforeIndex) || rider.getLocation().equals(afterIndex)){
                    rider.addNotification(new Notification(tempOrders));
                    break;
                }
            }
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
