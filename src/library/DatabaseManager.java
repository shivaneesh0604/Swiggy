package library;

import java.util.HashMap;

public class DatabaseManager {

    Database database = Database.getInstance();
    private int userID =1000;
    public User addCustomer(String userName, String passWord, Role role, String name) {
        User user = new Customer("CUSTOMER_"+userID++,ApplicationFactory.getCustomerApplication(), role, name);
        return database.addUser(user,userName,passWord);
    }

    public User addRider(String userName, String passWord, Role role, String name) {
        User user = new Rider("RIDER_"+userID++,ApplicationFactory.getRiderApplication(), role, name);
        return database.addUser(user,userName,passWord);
    }

    public User addRestaurantManager(String userName, String passWord, Role role, String name, int restaurantID) {
        Restaurant restaurant = database.getRestaurant(restaurantID);
        User user = new RestaurantManager("RESTAURANT_MANAGER_"+userID++,restaurant, ApplicationFactory.getRestaurantManagerApplication(), role, name);
        return database.addUser(user,userName,passWord);
    }

    public User getUser(String username, String password) {
        return database.getUser(username,password);
    }


}
