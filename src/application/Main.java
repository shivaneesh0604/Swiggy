package application;

import library.DatabaseManager;

import java.util.Scanner;

final class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DatabaseManager databaseManager = new DatabaseManager();
        ApplicationUI applicationUI = new ApplicationUI(databaseManager);
        Mainloop:
        while (true) {
            System.out.println("enter 1 for login 2 for signup other number for exit");
            String applicationUIAccess = sc.nextLine();
            try {
                if (Integer.parseInt(applicationUIAccess) == 1) {
                    applicationUI.logIN();
                } else if (Integer.parseInt(applicationUIAccess) == 2) {
                    applicationUI.signUP();
                } else {
                    break Mainloop;
                }
            } catch (NumberFormatException e) {
                System.out.println("enter 1 or 2");
            }
        }
    }
}