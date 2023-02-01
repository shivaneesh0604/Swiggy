package library;

import java.util.ArrayList;

public class Item {
    private final String foodName;
    private double price;
    private final Dietary dietary;
    private final Course course;
    private ArrayList<Timing> timing = new ArrayList<>();

    Item(String foodName, double price, Dietary dietary, Course course, Timing timing) {
        this.foodName = foodName.toUpperCase();
        this.price = price;
        this.dietary = dietary;
        this.course = course;
        this.timing.add(timing);
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
        return timing;
    }

    public boolean checkTiming(Timing timing) {
        for (Timing timing1 : this.timing) {
            if (timing1 == timing) {
                return true;
            }
        }
        return false;
    }

    void setTiming(Timing timing) {
        this.timing.add(timing);
    }
}
