package application;


import library.*;

import java.util.Scanner;

final class ApplicationUI {

    Scanner sc = new Scanner(System.in);
    private final DatabaseManager databaseManager;

    public ApplicationUI(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void logIN() {
        System.out.println("enter user name");
        String userName = sc.nextLine();
        System.out.println("enter password");
        String passWord = sc.nextLine();
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

    public void signUP() {
        System.out.println("welcome to login page");
        System.out.println("enter user name");
        String userName = sc.nextLine();
        System.out.println("enter password");
        String passWord = sc.nextLine();
        System.out.println("enter name");
        String name = sc.nextLine();
        System.out.println("enter which role you need to register");
        Utils.print(Role.values());
        int registerRole = Utils.inputVerification(Role.values().length);
        Role role = Role.values()[registerRole];
        switch (role) {
            case CUSTOMER:
                UserAddition userAddition = databaseManager.addCustomer(userName, passWord, role, name);
                if(userAddition.equals(UserAddition.USER_ADDED)){
                    logIN();
                }
                else {
                    System.out.println(userAddition);
                }
                break;
            case RIDER:
                UserAddition userAddition1 =  databaseManager.addRider(userName, passWord, role, name);
                if (userAddition1.equals(UserAddition.USER_ADDED)){
                    logIN();
                }
                else {
                    System.out.println(userAddition1);
                }
                break;
        }
        System.out.println("added successfully");
    }
}