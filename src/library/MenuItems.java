package library;

import java.util.Collection;
import java.util.HashMap;

class MenuItems {
    private HashMap<String, Item> menuItems;

    MenuItems() {
        this.menuItems = new HashMap<>();
    }

    void addMenusItems(Item items) {
        menuItems.put(items.getFoodName(), items);
    }

    void alterMenuItems(String foodName, int price) {
        Item item = menuItems.get(foodName);
        if (item == null)
            return;
        item.setPrice(price);
    }

    void deleteMenuItems(String foodName) {
        menuItems.remove(foodName);
    }

    boolean checkFoodAvailability(String foodName, Timing timing) {
        Item item = menuItems.get(foodName);
        if (item == null) {
            return false;
        } else {
            return true;
        }
    }
    
    void setTimingForFood(String foodName, Timing timing) {
        Item item = menuItems.get(foodName);
        if (item == null)
            return;
        item.setTiming(timing);
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
        return menuItems;
    }
}
