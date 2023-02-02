package library;

import java.util.Collection;
import java.util.HashMap;

class MenuList {
    private HashMap<String, Item> totalItems;

    MenuList() {
        this.totalItems  = new HashMap<>();
    }

    void addMenusItems(Item items) {
        totalItems.put(items.getFoodName(), items);
    }

    void alterMenuItems(String foodName, int price) {
        Item item = totalItems.get(foodName);
        if (item == null)
            return;
        item.setPrice(price);
    }

    void deleteMenuItems(String foodName) {
        totalItems.remove(foodName);
    }

    boolean checkFoodAvailability(String foodName, Timing timing) {
        Item item = totalItems.get(foodName);
        if (item == null) {
            return false;
        } else {
            return true;
        }
    }
    
    void setTimingForFood(String foodName, Timing timing) {
        Item item = totalItems.get(foodName);
        if (item == null)
            return;
        item.setTiming(timing);
    }

    double getPrice(String foodName){
        Item item = totalItems.get(foodName);
        return item.getPrice();
    }

    HashMap<String, Item> getItems(Timing timing) {
        HashMap<String, Item> availableTimingItems = new HashMap<>();
        Collection<Item> menuItems_values = totalItems.values();
        for (Item item : menuItems_values) {
            if (item.checkTiming(timing)) {
                availableTimingItems.put(item.getFoodName(), item);
            }
        }
        return availableTimingItems;
    }

    HashMap<String, Item> getItems() {
        return totalItems;
    }
}
