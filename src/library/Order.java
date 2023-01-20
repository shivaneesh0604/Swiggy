package library;

public class Order {
    private final String foodName;
    private int quantity;

    public Order(String foodName, int quantity) {
        this.foodName = foodName;
        this.quantity = quantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
