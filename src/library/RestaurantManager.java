package library;

import java.util.ArrayList;
import java.util.HashMap;

public final class RestaurantManager extends User {
    private final Restaurant restaurant;
    private final RestaurantManagerApplication applicationRestaurantManagerController;

    public RestaurantManager(String userID, String passWord, Restaurant restaurant, RestaurantManagerApplication applicationUI, Role role, String name) {
        super(userID, passWord, role, name);
        this.restaurant = restaurant;
        Item item = new Item("rice", 100, Dietary.VEG, Course.MAINCOURSE, Timing.AFTERNOON);
        Item item2 = new Item("chicken", 120, Dietary.NON_VEG, Course.MAINCOURSE, Timing.AFTERNOON);
        restaurant.getMenuList().addMenusItems(item);
        restaurant.getMenuList().addMenusItems(item2);
        this.applicationRestaurantManagerController = applicationUI;
    }

    public void addFoodToRestaurant(String foodName, double price, Dietary dietary, Course course, Timing timing) {
        Item item = new Item(foodName, price, dietary, course, timing);
        restaurant.getMenuList().addMenusItems(item);
    }

    public void alterMenuItems(String foodName, int price) {
        restaurant.getMenuList().alterMenuItems(foodName, price);
    }

    public void checkFoodAvailability(String foodName, Timing timing) {
        restaurant.getMenuList().checkFoodAvailability(foodName, timing);
    }

    public void setTimingForFood(String foodName, Timing timing) {
        restaurant.getMenuList().setTimingForFood(foodName, timing);
    }

    public void deleteMenuItems(String foodName) {
        restaurant.getMenuList().deleteMenuItems(foodName);
    }

    public HashMap<String, Item> getItems(Timing timing) {
        return restaurant.getMenuList().getItems(timing);
    }

    public HashMap<String, Item> getItems() {
        return restaurant.getMenuList().getItems();
    }

    public RestaurantStatus setRestaurantStatus(RestaurantStatus restaurantStatus) {
        return restaurant.setRestaurantStatus(restaurantStatus);
    }

    public HashMap<Integer, ArrayList<Order>> viewOrderGot() {
        return restaurant.viewOrderGot();
    }

    public String setStatusOfOrder(int orderID) {
        Status status = applicationRestaurantManagerController.getStatus(orderID);
        if (status == null) {
            return "wrong orderID";
        }
        return "status updated as " + applicationRestaurantManagerController.setStatusPrepared(orderID);
    }

}
