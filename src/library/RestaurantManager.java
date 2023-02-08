package library;

import java.util.ArrayList;
import java.util.HashMap;

public final class RestaurantManager extends User {
    private final Restaurant restaurant;
    private final RestaurantManagerApplication applicationRestaurantManagerController;

    RestaurantManager(String userID,Restaurant restaurant, RestaurantManagerApplication applicationUI, Role role, String name) {
        super(userID, role, name);
        this.restaurant = restaurant;
        this.applicationRestaurantManagerController = applicationUI;
    }

    public void addFoodToRestaurant(String foodName, double price, Dietary dietary, Course course, Timing timing) {
        Item item = new Item(foodName, price, dietary, course, timing);
        restaurant.getMenuList().addMenusItems(item);
    }

    public void alterMenuItems(Item item, int price) {
        restaurant.getMenuList().alterMenuItems(item, price);
    }

    public void checkFoodAvailability(String foodName, Timing timing) {
        restaurant.getMenuList().checkFoodAvailability(foodName, timing);
    }

    public void setTimingForFood(String foodName, Timing timing) {
        restaurant.getMenuList().setTimingForFood(foodName, timing);
    }

    public void deleteMenuItems(Item item) {
        restaurant.getMenuList().deleteMenuItems(item);
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

    public HashMap<Integer, ArrayList<LineOrder>> viewOrderGot() {
        return restaurant.viewOrderGot();
    }

    public String setStatus(int orderID,OrderStatus orderStatus) {
        OrderStatus orderStatus1 = applicationRestaurantManagerController.setStatusPrepared(orderID,orderStatus);
        if(orderStatus !=null){
            restaurant.setOrdersCompleted(orderID);
            return "status updated as " + orderStatus1;
        }
        return "wrong OrderID";
    }
}
