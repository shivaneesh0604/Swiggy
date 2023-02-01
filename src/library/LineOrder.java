package library;

public class LineOrder {
    private final String foodName;
    private int quantity;

    public LineOrder(String foodName, int quantity) {
        this.foodName = foodName.toUpperCase();
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
