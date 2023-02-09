package library;

import java.util.ArrayList;

public class Item {
    private final String foodName;
    private double price;
    private final Dietary dietary;
    private final Course course;
    private final ArrayList<Timing> timing = new ArrayList<>();

    Item(String foodName, double price, Dietary dietary, Course course, Timing timing) {
        this.foodName = foodName.toUpperCase();
        this.price = price;
        this.dietary = dietary;
        this.course = course;
        this.timing.add(timing);
    }

    public ArrayList<Timing> getTiming() {
        return new ArrayList<>(timing);
    }

    public String getFoodName() {
        return foodName;
    }

    public double getPrice() {
        return price;
    }

    RestaurantManagerReturnFunctions setPrice(double price) {
        this.price = price;
        return RestaurantManagerReturnFunctions.PRICE_CHANGED;
    }

    public Dietary getDietary() {
        return dietary;
    }

    public Course getCourse() {
        return course;
    }

    public boolean checkTiming(Timing timing) {
        for (Timing timing1 : this.timing) {
            if (timing1.equals(timing)) {
                return true;
            }
        }
        return false;
    }

    RestaurantManagerReturnFunctions setTiming(Timing timing) {
        this.timing.add(timing);
        return RestaurantManagerReturnFunctions.TIMING_ADDED;
    }

    RestaurantManagerReturnFunctions removeTiming(Timing timing){
        this.timing.remove(timing);
        return RestaurantManagerReturnFunctions.TIMING_REMOVED;
    }
}
