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
                case ADD_FOOD_TO_RESTAURANT: {
                    addFoodToRestaurant(items);
                    break;
                }

                case ALTER_MENU_ITEMS: {
                    alterMenuItems(items);
                    break;
                }

                case ADD_TIMING_FOR_FOOD: {
                    addTimingForFood(items);
                    break;
                }

                case REMOVE_TIMING_FOR_FOOD: {
                    removeTimingForFood(items);
                    break;
                }

                case DELETE_MENU_ITEMS: {
                    deleteMenuItems(items);
                    break;
                }

                case SHOW_ALL_FOOD_ITEMS: {
                    printFoodItems(items);
                    break;
                }

                case GET_TIME_SPECIFIC_FOOD_ITEMS: {
                    getTimeSpecificFoodItems();
                    break;
                }

                case SET_RESTAURANT_STATUS: {
                    setRestaurantStatus();
                    break;
                }

                case VIEW_ORDERS_GOT: {
                    HashMap<Integer, ArrayList<LineOrder>> order = restaurantManager.viewOrderGot();
                    viewOrder(order);
                    break;
                }

                case SET_STATUS_PREPARING: {
                    setStatus(OrderStatus.PREPARING);
                    break;
                }

                case SET_STATUS_PREPARED: {
                    setStatus(OrderStatus.PREPARED);
                    break;
                }

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

    private void addFoodToRestaurant(HashMap<String,Item> items){
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
    }

    private void alterMenuItems(HashMap<String,Item> items){
        System.out.println("available items are");
        printFoodItems(items);
        System.out.println("enter foodName to which you are going to alter in restaurant");
        String foodName = sc.nextLine().toUpperCase();
        if (items.containsKey(foodName)) {
            System.out.println("enter price to alter ");
            System.out.println("the original price before altering is " + items.get(foodName).getPrice());
            double price = sc.nextDouble();
            System.out.println(restaurantManager.alterMenuItems(items.get(foodName), price));
        } else {
            System.out.println("the foodName" + foodName + " is not available this restaurant");
        }
    }

    private void addTimingForFood(HashMap<String,Item> items){
        System.out.println("available items are");
        printFoodItems(items);
        System.out.println("enter which food you need to set timing for");
        String foodName = sc.nextLine().toUpperCase();
        Item item = restaurantManager.getItems().get(foodName);
        if (item != null) {
            ArrayList<Timing> timing3 = item.getTiming();
            if (timing3 == null) {
                System.out.println("the foodName" + foodName + " is not available this restaurant");
                return;
            }
            if (timing3.size() > 0) {
                System.out.println(foodName + " has timing of ");
                for (Timing timing1 : timing3) {
                    System.out.println(timing1);
                }
            } else {
                System.out.println("no timing set for this foodItem");
            }
            System.out.println("which timing to add");
            Utils.print(Timing.values());
            int timingOption = Utils.inputVerification(Timing.values().length);
            Timing timing = Timing.values()[timingOption];
            if (timing3.contains(timing)) {
                System.out.println("already has this timing so select another timing other than " + timing);
                return;
            }
            System.out.println(restaurantManager.setTimingForFood(foodName, timing));
        } else {
            System.out.println("wrong foodName");
        }
    }

    private void removeTimingForFood(HashMap<String,Item> items){
        System.out.println("available items are");
        printFoodItems(items);
        System.out.println("enter which food you need to remove timing for");
        String foodName = sc.nextLine().toUpperCase();
        ArrayList<Timing> timings = restaurantManager.getItems().get(foodName).getTiming();
        if (timings == null) {
            System.out.println("the foodName" + foodName + " is not available this restaurant");
            return;
        }
        if (timings.size() > 0) {
            System.out.println(foodName + " has timing of ");
            for (Timing timing1 : timings) {
                System.out.println(timing1);
            }
        } else {
            System.out.println("no timing set for this foodItem");
        }
        System.out.println("which timing to change");
        Utils.print(Timing.values());
        int timingOption = Utils.inputVerification(Timing.values().length);
        Timing timing = Timing.values()[timingOption];
        if (timings.contains(timing)) {
            System.out.println(restaurantManager.removeTimingForFood(foodName, timing));
        } else {
            System.out.println("cant process since this Timing is not set for this food Item");
        }
    }

    private void deleteMenuItems(HashMap<String,Item> items){
        System.out.println("available items are");
        printFoodItems(items);
        System.out.println("enter which food to delete from menu");
        String foodName = sc.nextLine().toUpperCase();
        if (items.containsKey(foodName)) {
            System.out.println(restaurantManager.deleteMenuItems(items.get(foodName)));
        } else {
            System.out.println("no food with this foodName to delete");
        }
    }

    private void getTimeSpecificFoodItems(){
        System.out.println("enter which timing you need the food Items");
        Utils.print(Timing.values());
        int timingOption1 = Utils.inputVerification(Timing.values().length);
        Timing timing1 = Timing.values()[timingOption1];
        HashMap<String, Item> items = restaurantManager.getItems(timing1);
        if (items.size() > 0) {
            printFoodItems(items);
        } else {
            System.out.println("no items found in this timing");
        }
    }

    private void setRestaurantStatus(){
        System.out.println(restaurantManager.getRestaurant().getRestaurantStatus() + " is the status of restaurant");
        System.out.println("enter to which status to change");
        Utils.print(RestaurantStatus.values());
        int restaurantStatusOption = Utils.inputVerification(RestaurantStatus.values().length);
        RestaurantStatus restaurantStatus = RestaurantStatus.values()[restaurantStatusOption];
        System.out.println("status is "+restaurantManager.setRestaurantStatus(restaurantStatus));
    }

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
                System.out.println("food name is "+lineOrder2.getItem().getFoodName()+" quantity is "+lineOrder2.getQuantity());
            }
        }
    }

    private void setStatus(OrderStatus orderStatus){
        HashMap<Integer, ArrayList<LineOrder>> order = restaurantManager.viewOrderGot();
        viewOrder(order);
        System.out.println("enter orderID to set order as preparing");
        int orderID = sc.nextInt();
        OrderStatus orderStatusChanged = restaurantManager.setStatus(orderID,orderStatus);
        if (orderStatusChanged == null) {
            System.out.println("wrong orderID");
            return;
        }
        System.out.println("order status changed to " + orderStatusChanged);
    }

    private void printFoodItems(HashMap<String, Item> items) {
        Collection<Item> items1 = items.values();
        for (Item item : items1) {
            System.out.println("foodName is " + item.getFoodName() + " price is " + item.getPrice() + " course is " + item.getCourse() + " dietary is " + item.getDietary());
        }
    }
}
