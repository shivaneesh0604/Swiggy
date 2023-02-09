package application;

import java.util.Scanner;

public class Utils {
    static Scanner sc =new Scanner(System.in);
    public static void print(Enum[] enums) {
        for (int i = 0; i < enums.length; i++) {
            System.out.println(i + "," + enums[i]);
        }
    }
    public static int inputVerification(int noOfOptions) {
        String input = sc.nextLine();
        if (input.matches("[0-9]+") && (Integer.parseInt(input) < noOfOptions)) {
            return Integer.parseInt(input);
        } else {
            System.out.println("enter between 0 and "+(noOfOptions-1));
            return inputVerification(noOfOptions);
        }
    }
}