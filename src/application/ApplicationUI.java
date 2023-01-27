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
                    UI ui = new CustomerUI((Customer) user);
                    ui.entersUI();
                    break;
                case RIDER:
                    UI ui2 = new RiderUI((Rider) user);
                    ui2.entersUI();
                    break;
                case RESTAURANT_MANAGER:
                    UI ui3 = new RestaurantManagerUI((RestaurantManager) user);
                    ui3.entersUI();
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
                UI ui = new CustomerUI(user);
                ui.entersUI();
                break;
            case RIDER:
                Rider rider = (Rider) databaseManager.addRider(userName, passWord, role, application, name);
                UI ui1 = new RiderUI(rider);
                ui1.entersUI();
                break;

            case RESTAURANT_MANAGER:
                int restaurantID = 1;
                RestaurantManager restaurantManager = (RestaurantManager) databaseManager.addRestaurantManager(userName, passWord, role, application, name, restaurantID);
                UI ui2 = new RestaurantManagerUI(restaurantManager);
                ui2.entersUI();
                break;
        }
        System.out.println("added successfully");
    }
}