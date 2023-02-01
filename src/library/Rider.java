package library;

import java.util.ArrayList;

public final class Rider extends User {
    private final RiderApplication riderApplication;
    private RiderStatus riderStatus;
    private OrderList orderList;
    private ArrayList<Notification> notification;
    private Location location;

    Rider(String userID, RiderApplication application, Role role, String name) {
        super(userID, role, name);
        this.riderApplication = application;
        this.riderStatus = RiderStatus.AVAILABLE;
    }

    public RiderAcceptance acceptOrder(Notification notification1) {
        if (!notification1.getOrderList().getStatus().equals(Status.CANCELLED)) {
            RiderAcceptance riderAcceptance = riderApplication.acceptOrder(orderList.getOrderID());
            if (riderAcceptance.equals(RiderAcceptance.ACCEPTED)) {
                this.orderList = notification1.getOrderList();
                this.notification.clear();
                this.riderStatus = RiderStatus.NOT_AVAILABLE;
                return orderList.getRiderAcceptance();
            }
            else {
                return RiderAcceptance.NOT_ACCEPTED;
            }
        }
        return null;
    }

    public void declineOrder(Notification notification) {
        this.notification.remove(notification);
    }

    public String receiveOrderFromRestaurant() {
        if (orderList != null) {
            Status status = orderList.getStatus();
            if (status.equals(Status.PREPARED)) {
                return "Status changed to " + riderApplication.receiveOrderFromRestaurant(orderList.getOrderID());
            } else if (status.equals(Status.CANCELLED)) {
                this.orderList = null;
                this.riderStatus = RiderStatus.AVAILABLE;
                return "that order is cancelled so cant process";
            } else if (status.equals(Status.PREPARING)) {
                return "wait till the order is prepared";
            }
        }
        return "cant receive since no order is accepted by rider.... first accept an order";
    }

    public String deliverFood() {
        if (orderList != null) {
            Status status = orderList.getStatus();
            if (status.equals(Status.PICKED)) {
                Status status1 = riderApplication.deliverFoodToCustomer(orderList.getOrderID());
                this.riderStatus = RiderStatus.AVAILABLE;
                this.orderList = null;
                return status1 + " the food";
            } else if (status.equals(Status.CANCELLED)) {
                this.orderList = null;
                this.riderStatus = RiderStatus.AVAILABLE;
                return "order is cancelled  ";
            }
        }
        return "cant deliver since no food picked by this rider...first accept an order";
    }

    public String cancelOrder() {
        if (orderList != null) {
            RiderAcceptance riderAcceptance = riderApplication.declineOrder(orderList);
            this.orderList = null;
            this.riderStatus = RiderStatus.AVAILABLE;
            return "the order is cancelled by the rider and changed rider acceptance to " + riderAcceptance;
        }
        return null;
    }

    void addNotification(Notification notification) {
        if (riderStatus.equals(RiderStatus.AVAILABLE))
            this.notification.add(notification);
    }

    void setLocation(Location location){
        this.location = location;
    }

    public ArrayList<Notification> getNotification() {
        return notification;
    }

    public RiderStatus getRiderStatus() {
        return riderStatus;
    }

    public Location getLocation() {
        return location;
    }
}
