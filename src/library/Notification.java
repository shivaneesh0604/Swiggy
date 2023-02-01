package library;

public class Notification {
    private final OrderList orderList;

    public Notification(OrderList orderList) {
        this.orderList = orderList;
    }

    public OrderList getOrderList() {
        return orderList;
    }
}
