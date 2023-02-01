package library;

import java.util.ArrayList;

public class Notification {
    private final Order order;

    private ArrayList<String> cancelledRiderIds;

    public Notification(Order order) {
        this.order = order;
        cancelledRiderIds = new ArrayList<>();
    }

    public boolean checkCancelledRiderIds(String userID) {
        if (cancelledRiderIds.contains(userID)) {
            return true;
        }
        return false;
    }

    public ArrayList<String> getCancelledRiderIds() {
        return cancelledRiderIds;
    }

    public void setCancelledRiderIds(String cancelledRiderIds) {
        this.cancelledRiderIds.add(cancelledRiderIds);
    }

    public Order getOrderList() {
        return order;
    }
}
