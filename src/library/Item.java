package library;

import java.util.ArrayList;

public class Item {
    private final String foodName;
    private double price;
    private final Dietary dietary;
    private final Course course;
    private ArrayList<Timing> timings;

    public Item(String foodName, double price, Dietary dietary, Course course, Timing timing) {
        this.foodName = foodName.toUpperCase();
        this.price = price;
        this.dietary = dietary;
        this.course = course;
        this.timings.add(timing);
    }

    public String getFoodName() {
        return foodName;
    }

    public double getPrice() {
        return price;
    }

    void setPrice(int price) {
        this.price = price;
    }

    public Dietary getDietary() {
        return dietary;
    }

    public Course getCourse() {
        return course;
    }

    public ArrayList<Timing> getTiming() {
        return timings;
    }

    public boolean checkTiming(Timing timing) {
        for (Timing timing1 : this.timings) {
            if (timing1 == timing) {
                return true;
            }
        }
        return false;
    }

    void setTiming(Timing timing) {
        this.timings.add(timing);
    }
}
