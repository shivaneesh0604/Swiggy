package application;

import library.Application;
import library.Database;
import library.DatabaseManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Application application = new Application();
        DatabaseManager databaseManager = new DatabaseManager();
        Database.getInstance().init(application);
        ApplicationUI applicationUI = new ApplicationUI(databaseManager);
        while (true) {
            System.out.println("enter 1 for login 2 for signup");
            int applicationUIAccess = sc.nextInt();
            if (applicationUIAccess == 1) {
                System.out.println("enter user name");
                String userName = sc.nextLine();
                System.out.println("enter password");
                String passWord = sc.nextLine();
                applicationUI.logIN(userName, passWord);
                break;
            } else if (applicationUIAccess == 2) {
                applicationUI.signUP(application);
                break;
            }
        }
    }
}