package library;

import java.util.ArrayList;

public class Notification {
    private final Order order;
    private ArrayList<String> cancelledRiderIds;

    Notification(Order order) {
        this.order = order;
        cancelledRiderIds = new ArrayList<>();
    }

    boolean checkCancelledRiderIds(String userID) {
        if (cancelledRiderIds.contains(userID)) {
            return true;
        }
        return false;
    }

    ArrayList<String> getCancelledRiderIds() {
        return cancelledRiderIds;
    }

    void setCancelledRiderIds(String cancelledRiderIds) {
        this.cancelledRiderIds.add(cancelledRiderIds);
    }

    public Order getOrder() {
        return order;
    }
}