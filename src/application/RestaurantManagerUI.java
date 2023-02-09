package application;

import library.*;

import java.util.*;

final class RestaurantManagerUI implements UI {
    Scanner sc = new Scanner(System.in);
    private final RestaurantManager restaurantManager;

    RestaurantManagerUI(RestaurantManager restaurantManager) {
        this.restaurantManager = restaurantManager;
    }

    public void entersUI() {
        MainLoop:
        while (true) {
            HashMap<String, Item> items = restaurantManager.getItems();
            Utils.print(RestaurantManagerOptions.values());
            int restaurantManagerOptions = Utils.inputVerification(RestaurantManagerOptions.values().length);
            RestaurantManagerOptions restaurantManagerOptions1 = RestaurantManagerOptions.values()[restaurantManagerOptions];
            switch (restaurantManagerOptions1) {
                case ADD_FOOD_TO_RESTAURANT:
                    System.out.println("enter food name to add");
                    String foodName = sc.nextLine();
                    System.out.println("enter price for " + foodName);
                    double price = sc.nextDouble();
                    sc.nextLine();
                    System.out.println("enter which dietary it is");
                    Utils.print(Dietary.values());
                    int dietaryOption = Utils.inputVerification(Dietary.values().length);
                    Dietary dietary = Dietary.values()[dietaryOption];
                    Utils.print(Course.values());
                    int courseOption = Utils.inputVerification(Course.values().length);
                    Course course = Course.values()[courseOption];
                    Utils.print(Timing.values());
                    int timingOption = Utils.inputVerification(Timing.values().length);
                    Timing timing = Timing.values()[timingOption];
                    System.out.println(restaurantManager.addFoodToRestaurant(foodName, price, dietary, course, timing));
                    break;

                case ALTER_MENU_ITEMS:
                    System.out.println("available items are");
                    printFoodItems(items);
                    System.out.println("enter foodName to which you are going to alter in restaurant");
                    String foodName1 = sc.nextLine().toUpperCase();
                    if (items.containsKey(foodName1)) {
                        System.out.println("enter price to alter ");
                        System.out.println("the original price before altering is " + items.get(foodName1).getPrice());
                        Double price1 = sc.nextDouble();
                        System.out.println(restaurantManager.alterMenuItems(items.get(foodName1), price1));
                    } else {
                        System.out.println("the foodName" + foodName1 + " is not available this restaurant");
                    }
                    break;

                case ADD_TIMING_FOR_FOOD:
                    System.out.println("available items are");
                    printFoodItems(items);
                    System.out.println("enter which food you need to set timing for");
                    String foodName2 = sc.nextLine().toUpperCase();
                    Item item = restaurantManager.getItems().get(foodName2);
                    if (item != null) {
                        ArrayList<Timing> timing3 = item.getTiming();
                        if (timing3 == null) {
                            System.out.println("the foodName" + foodName2 + " is not available this restaurant");
                            break;
                        }
                        if (timing3.size() > 0) {
                            System.out.println(foodName2 + " has timing of ");
                            for (Timing timing1 : timing3) {
                                System.out.println(timing1);
                            }
                        } else {
                            System.out.println("no timing set for this foodItem");
                        }
                        System.out.println("which timing to add");
                        Utils.print(Timing.values());
                        int timingOption2 = Utils.inputVerification(Timing.values().length);
                        Timing timing2 = Timing.values()[timingOption2];
                        if (timing3.contains(timing2)) {
                            System.out.println("already has this timing so select another timing other than " + timing2);
                            break;
                        }
                        System.out.println(restaurantManager.setTimingForFood(foodName2, timing2));
                    } else {
                        System.out.println("wrong foodName");
                    }
                    break;

                case REMOVE_TIMING_FOR_FOOD:
                    System.out.println("available items are");
                    printFoodItems(items);
                    System.out.println("enter which food you need to remove timing for");
                    String foodName3 = sc.nextLine().toUpperCase();
                    ArrayList<Timing> timing4 = restaurantManager.getItems().get(foodName3).getTiming();
                    if (timing4 == null) {
                        System.out.println("the foodName" + foodName3 + " is not available this restaurant");
                        break;
                    }
                    if (timing4.size() > 0) {
                        System.out.println(foodName3 + " has timing of ");
                        for (Timing timing1 : timing4) {
                            System.out.println(timing1);
                        }
                    } else {
                        System.out.println("no timing set for this foodItem");
                    }
                    System.out.println("which timing to change");
                    Utils.print(Timing.values());
                    int timingOption3 = Utils.inputVerification(Timing.values().length);
                    Timing timing5 = Timing.values()[timingOption3];
                    if (timing4.contains(timing5)) {
                        System.out.println(restaurantManager.removeTimingForFood(foodName3, timing5));
                    } else {
                        System.out.println("cant process since this Timing is not set for this food Item");
                    }
                    break;

                case DELETE_MENU_ITEMS:
                    System.out.println("available items are");
                    printFoodItems(items);
                    System.out.println("enter which food to delete from menu");
                    String foodName4 = sc.nextLine().toUpperCase();
                    if (items.containsKey(foodName4)) {
                        System.out.println(restaurantManager.deleteMenuItems(items.get(foodName4)));
                    } else {
                        System.out.println("no food with this foodName to delete");
                    }
                    break;

                case PRINT_ALL_FOOD_ITEMS:
                    printFoodItems(items);
                    break;

                case GET_TIME_SPECIFIC_FOOD_ITEMS:
                    System.out.println("enter which timing you need the food Items");
                    Utils.print(Timing.values());
                    int timingOption1 = Utils.inputVerification(Timing.values().length);
                    Timing timing1 = Timing.values()[timingOption1];
                    HashMap<String, Item> items2 = restaurantManager.getItems(timing1);
                    if (items2.size() > 0) {
                        printFoodItems(items2);
                    }
                    else {
                        System.out.println("no items found in this timing");
                    }
                    break;

                case SET_RESTAURANT_STATUS:
                    System.out.println(restaurantManager.getRestaurant().getRestaurantStatus() + " is the status of restaurant");
                    System.out.println("enter to which status to change");
                    Utils.print(RestaurantStatus.values());
                    int restaurantStatusOption = Utils.inputVerification(RestaurantStatus.values().length);
                    RestaurantStatus restaurantStatus = RestaurantStatus.values()[restaurantStatusOption];
                    System.out.println(restaurantManager.setRestaurantStatus(restaurantStatus));
                    break;

                case VIEW_ORDERS_GOT:
                    HashMap<Integer, ArrayList<LineOrder>> order = restaurantManager.viewOrderGot();
                    viewOrder(order);
                    break;

                case SET_STATUS_PREPARING:
                    System.out.println("enter orderID to prepared");
                    int orderID = sc.nextInt();
                    OrderStatus orderStatus = restaurantManager.setStatus(orderID, OrderStatus.PREPARING);
                    if (orderStatus == null) {
                        System.out.println("wrong orderID");
                        break;
                    }
                    System.out.println("order status changed to " + orderStatus);
                    break;

                case SET_STATUS_PREPARED:
                    System.out.println("enter orderID to prepared");
                    int orderID1 = sc.nextInt();
                    OrderStatus orderStatus1 = restaurantManager.setStatus(orderID1, OrderStatus.PREPARED);
                    if (orderStatus1 == null) {
                        System.out.println("wrong orderID");
                        break;
                    }
                    System.out.println("order status change to " + orderStatus1);
                    break;

                case BACK:
                    break MainLoop;
            }
        }
    }
//        System.out.println(restaurantManager.setRestaurantStatus(RestaurantStatus.NOT_AVAILABLE));
//        HashMap<Integer, ArrayList<LineOrder>> order = restaurantManager.viewOrderGot();
//        System.out.println("orders got are");
//        viewOrder(order);
//        System.out.println("enter orderID to prepared");
//        int orderID = sc.nextInt();
//        System.out.println(restaurantManager.setStatus(orderID, OrderStatus.PREPARING));
//        System.out.println(restaurantManager.setStatus(orderID,OrderStatus.PREPARED));

    private void viewOrder(HashMap<Integer, ArrayList<LineOrder>> order) {
        if (order == null) {
            System.out.println("no orders found ");
            return;
        }
        System.out.println("orders got are");
        for (Map.Entry<Integer, ArrayList<LineOrder>> entry : order.entrySet()) {
            ArrayList<LineOrder> lineOrder1 = entry.getValue();
            System.out.println("orderID is : " + entry.getKey());
            for (LineOrder lineOrder2 : lineOrder1) {
            }
        }
    }

    private void printFoodItems(HashMap<String, Item> items) {
        Collection<Item> items1 = items.values();
        for (Item item : items1) {
            System.out.println("foodName is " + item.getFoodName() + " price is " + item.getPrice() + " course is " + item.getCourse() + " dietary is " + item.getDietary());
        }
    }
}
