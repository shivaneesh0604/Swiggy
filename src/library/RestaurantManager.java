package library;

import java.util.ArrayList;
import java.util.HashMap;

public final class RestaurantManager extends User {
    private final Restaurant restaurant;
    private final RestaurantManagerApplication applicationRestaurantManagerController;

    RestaurantManager(String userID, Restaurant restaurant, RestaurantManagerApplication applicationUI, Role role, String name) {
        super(userID, role, name);
        this.restaurant = restaurant;
        this.applicationRestaurantManagerController = applicationUI;
    }

    public RestaurantManagerReturnFunctions addFoodToRestaurant(String foodName, double price, Dietary dietary, Course course, Timing timing) {
        Item item = new Item(foodName, price, dietary, course, timing);
        return restaurant.getMenuList().addMenusItems(item);
    }

    public RestaurantManagerReturnFunctions alterMenuItems(Item item, double price) {
        return restaurant.getMenuList().alterMenuItems(item, price);
    }

//    public void checkFoodAvailability(String foodName, Timing timing) {
//        restaurant.getMenuList().checkFoodAvailability(foodName, timing);
//    }

    public RestaurantManagerReturnFunctions setTimingForFood(String foodName, Timing timing) {
        return restaurant.getMenuList().setTimingForFood(foodName, timing);
    }

    public RestaurantManagerReturnFunctions removeTimingForFood(String foodName,Timing timing){
        return restaurant.getMenuList().removeTimingForFood(foodName,timing);
    }

    public RestaurantManagerReturnFunctions deleteMenuItems(Item item) {
        return restaurant.getMenuList().deleteMenuItems(item);
    }

    public HashMap<String, Item> getItems(Timing timing) {
        return restaurant.getMenuList().getItems(timing);
    }

    public RestaurantStatus setRestaurantStatus(RestaurantStatus restaurantStatus) {
        return restaurant.setRestaurantStatus(restaurantStatus);
    }

    public HashMap<Integer, ArrayList<LineOrder>> viewOrderGot() {
        return restaurant.viewOrderGot();
    }

    public OrderStatus setStatus(int orderID,OrderStatus orderStatus) {
        OrderStatus orderStatus1 = applicationRestaurantManagerController.setStatusPrepared(orderID,orderStatus);
        if(orderStatus !=null){
            restaurant.setOrdersCompleted(orderID);
            return  orderStatus1;
        }
        return null;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public HashMap<String, Item> getItems() {
        return restaurant.getMenuList().getItems();
    }
}
