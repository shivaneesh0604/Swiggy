package library;

import java.util.ArrayList;

public class Notification {
    private final Order order;
    private final ArrayList<String> cancelledRiderIds;

    Notification(Order order) {
        this.order = order;
        cancelledRiderIds = new ArrayList<>();
    }

    boolean checkCancelledRiderIds(String userID) {
        return !cancelledRiderIds.contains(userID);
    }

    void setCancelledRiderIds(String cancelledRiderIds) {
        this.cancelledRiderIds.add(cancelledRiderIds);
    }

    public Order getOrder() {
        return order;
    }
}