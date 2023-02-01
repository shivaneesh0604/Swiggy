package library;

public class Notification {
    private final Order order;

    public Notification(Order order) {
        this.order = order;
    }

    public Order getOrderList() {
        return order;
    }
}
