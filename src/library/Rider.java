package library;

import java.util.ArrayList;

public final class Rider extends User {
    private final RiderApplication riderApplication;
    private RiderStatus riderStatus;
    private Order order;
    private ArrayList<Notification> notification;
    private Location location;

    Rider(String userID, RiderApplication application, Role role, String name) {
        super(userID, role, name);
        this.riderApplication = application;
        this.riderStatus = RiderStatus.NOT_AVAILABLE;
        notification = new ArrayList<>();
    }

    public RiderFunctionalityStatus acceptOrder(Notification notification1) {
        this.order = notification1.getOrder();
        RiderFunctionalityStatus riderFunctionalityStatus = riderApplication.acceptOrder(this, order.getOrderID());
        if (riderFunctionalityStatus.equals(RiderFunctionalityStatus.ACCEPTED)) {
            this.riderStatus = RiderStatus.NOT_AVAILABLE;
            this.notification = null;
            return order.getRiderFunctionalityStatus();
        } else {
            return RiderFunctionalityStatus.NOT_ACCEPTED;
        }
    }

    public RiderFunctionalityStatus declineOrder(Notification notification) {
        return riderApplication.declineOrder(this, notification);
    }

    public RiderReturnFunctionalities changeStatusToPicked() {
        if (order != null) {
            OrderStatus orderStatus = order.getStatus();
            if (orderStatus.equals(OrderStatus.PREPARED)) {
                riderApplication.changeStatusByRider(order, RiderFunctionalityStatus.PICKED);
                return RiderReturnFunctionalities.PICKED;
            } else if (orderStatus.equals(OrderStatus.CANCELLED)) {
                this.order = null;
                this.riderStatus = RiderStatus.AVAILABLE;
                return RiderReturnFunctionalities.THAT_ORDER_IS_CANCELLED;
            } else if (orderStatus.equals(OrderStatus.ORDER_PLACED)) {
                return RiderReturnFunctionalities.WAIT_TILL_ORDER_IS_PREPARED;
            }
        }
        return RiderReturnFunctionalities.CANT_PROCESS_SINCE_NO_ORDER_IS_ACCEPTED;
    }

    public RiderReturnFunctionalities changeStatusToDelivered() {
        if (order != null) {
            RiderFunctionalityStatus riderFunctionalityStatus = order.getRiderFunctionalityStatus();
            if (riderFunctionalityStatus.equals(RiderFunctionalityStatus.PICKED)) {
                riderApplication.changeStatusByRider(order, RiderFunctionalityStatus.DELIVERED);
                this.riderStatus = RiderStatus.AVAILABLE;
                this.order = null;
                return RiderReturnFunctionalities.DELIVERED;
            } else if (order.getStatus().equals(OrderStatus.CANCELLED)) {
                this.order = null;
                this.riderStatus = RiderStatus.AVAILABLE;
                return RiderReturnFunctionalities.THAT_ORDER_IS_CANCELLED;
            }
        }
        return RiderReturnFunctionalities.CANT_PROCESS_SINCE_NO_ORDER_IS_ACCEPTED;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    void addNotification(Notification notification) {
        if (riderStatus.equals(RiderStatus.AVAILABLE))
            this.notification.add(notification);
    }

    public ArrayList<Notification> getNotification() {
        return new ArrayList<>(notification);
    }

    void removeNotification(int orderID) {
        notification.removeIf(notification1 -> notification1.getOrder().getOrderID() == orderID);
    }

    public Order getOrder() {
        return order;
    }

    public RiderStatus setRiderStatus(RiderStatus riderStatus) {
        if (this.order == null) {
            this.riderStatus = riderStatus;
            riderApplication.setNotification(this);
            return this.riderStatus;
        } else {
            System.out.println("hi in rider");
            return RiderStatus.AVAILABLE;
        }
    }

    public RiderStatus getRiderStatus() {
        return riderStatus;
    }

    public Location getLocation() {
        return location;
    }
}
