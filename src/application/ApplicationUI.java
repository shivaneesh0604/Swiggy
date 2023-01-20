package application;


import library.Customer;
import library.Application;
import library.DatabaseManager;
import library.RestaurantManager;
import library.Rider;
import library.User;
import library.Role;

public class ApplicationUI {

    private final DatabaseManager databaseManager;

    public ApplicationUI(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void logIN(String userName, String passWord) {
        User user = databaseManager.getUser(userName, passWord);
        if (user != null) {
            Role role = user.getRole();
            switch (role) {
                case CUSTOMER:
                    new CustomerUI((Customer) user).entersUI();
                    break;
                case RESTAURANT_MANAGER:
                    new RestaurantManagerUI((RestaurantManager) user).entersUI();
                    break;
                case RIDER:
                    new RiderUI((Rider) user).entersUI();
                    break;
            }
        }
        else {
            System.out.println("invalid credentials");
        }
    }

    public void signUP(Application application) {
        String userName = "shiva123";
        String passWord = "password";
        Role role = Role.CUSTOMER;
        String name = "shiva";
        switch (role) {
            case CUSTOMER:
                Customer user = (Customer) databaseManager.addCustomer(userName, passWord, role, application, name);
                new CustomerUI(user).entersUI();
            case RIDER:
                Rider rider = (Rider) databaseManager.addRider(userName, passWord, role, application, name);
                new RiderUI(rider).entersUI();
            case RESTAURANT_MANAGER:
                int restaurantID = 1;
                RestaurantManager restaurantManager = (RestaurantManager) databaseManager.addRestaurantManager(userName, passWord, role, application, name, restaurantID);
                new RestaurantManagerUI(restaurantManager).entersUI();
        }
        System.out.println("added successfully");
    }
}