package library;

import java.util.HashMap;

public class DatabaseManager {

    Database database = Database.getInstance();

    public User addCustomer(String userName, String passWord, Role role, String name) {
        User user = new Customer(userName, passWord, ApplicationFactory.getCustomerApplication(), role, name);
        return database.addUser(user);
    }

    public User addRider(String userName, String passWord, Role role, String name) {
        User user = new Rider(userName, passWord, ApplicationFactory.getRiderApplication(), role, name);
        return database.addUser(user);
    }

    public User addRestaurantManager(String userName, String passWord, Role role, String name, int restaurantID) {
        Restaurant restaurant = database.getRestaurant(restaurantID);
        User user = new RestaurantManager(userName, passWord, restaurant, ApplicationFactory.getRestaurantManagerApplication(), role, name);
        return database.addUser(user);
    }

    public User getUser(String username, String password) {
        User user = database.getUser(username);
        if (user != null) {
            if (user.getUserID().equals(username) && user.getPassWord().equals(password)) {
                return user;
            }
        }
        return null;
    }


}
