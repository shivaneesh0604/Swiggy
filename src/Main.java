import application.ApplicationUI;
import library.Application;
import library.Bill;
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
        Mainloop : while (true) {
            while (true) {
                System.out.println("enter 1 for login 2 for signup other number for exit");
                int applicationUIAccess = sc.nextInt();
                if (applicationUIAccess == 1) {
                    System.out.println("enter user name");
                    sc.nextLine();
                    String userName = sc.nextLine();
                    System.out.println("enter password");
                    String passWord = sc.nextLine();
//                    System.out.println("user namr is "+userName+" password is "+passWord);
                    applicationUI.logIN(userName, passWord);
                    break;
                } else if (applicationUIAccess == 2) {
                    applicationUI.signUP(application);
                    break;
                }
                else{
                    break Mainloop;
                }
            }
        }
    }
}