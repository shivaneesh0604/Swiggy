package library;

import java.util.Collection;
import java.util.HashMap;

class MenuItems {
    private HashMap<String, Item> menuItems;

    MenuItems() {
        this.menuItems = new HashMap<>();
    }

    RestaurantManagerReturnFunctions addMenusItems(Item items) {
        menuItems.put(items.getFoodName(), items);
        return RestaurantManagerReturnFunctions.ITEM_ADDED;
    }

    RestaurantManagerReturnFunctions alterMenuItems(Item item, double price) {
        return item.setPrice(price);
    }

    RestaurantManagerReturnFunctions deleteMenuItems(Item item) {
        menuItems.remove(item.getFoodName());
        return RestaurantManagerReturnFunctions.ITEM_DELETED;
    }

//    boolean checkFoodAvailability(String foodName, Timing timing) {
//        Item item = menuItems.get(foodName);
//        if (item == null) {
//            return false;
//        } else {
//            return true;
//        }
//    }
    
    RestaurantManagerReturnFunctions setTimingForFood(String foodName, Timing timing) {
        Item item = menuItems.get(foodName);
        if (item == null)
            return null;
        return item.setTiming(timing);
    }

    RestaurantManagerReturnFunctions removeTimingForFood(String foodName,Timing timing){
        Item item = menuItems.get(foodName);
        if (item == null)
            return null;
        return item.removeTiming(timing);
    }

    HashMap<String, Item> getItems(Timing timing) {
        HashMap<String, Item> availableTimingItems = new HashMap<>();
        Collection<Item> menuItems_values = menuItems.values();
        for (Item item : menuItems_values) {
            if (item.checkTiming(timing)) {
                availableTimingItems.put(item.getFoodName(), item);
            }
        }
        return availableTimingItems;
    }

    HashMap<String, Item> getItems() {
        return new HashMap<>(menuItems);
    }
}
