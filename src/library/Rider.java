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
        this.riderStatus = RiderStatus.AVAILABLE;
    }

    public RiderAcceptance acceptOrder(Notification notification1) {
        if (!notification1.getOrderList().getStatus().equals(OrderStatus.CANCELLED)) {
            this.order = notification1.getOrderList();
            RiderAcceptance riderAcceptance = riderApplication.acceptOrder(order.getOrderID());
            if (riderAcceptance.equals(RiderAcceptance.ACCEPTED)) {
                this.notification.clear();
                this.riderStatus = RiderStatus.NOT_AVAILABLE;
                return order.getRiderAcceptance();
            } else {
                return RiderAcceptance.NOT_ACCEPTED;
            }
        }
        return null;
    }

    public void declineOrder(Notification notification) {
        riderApplication.declineOrder(notification.getOrderList(), this);
        this.notification.remove(notification);
    }

    public String receiveOrderFromRestaurant() {
        if (order != null) {
            OrderStatus orderStatus = order.getStatus();
            if (orderStatus.equals(OrderStatus.PREPARED)) {
                return "Status changed to " + riderApplication.receiveOrderFromRestaurant(order.getOrderID());
            } else if (orderStatus.equals(OrderStatus.CANCELLED)) {
                this.order = null;
                this.riderStatus = RiderStatus.AVAILABLE;
                return "that order is cancelled so cant process";
            } else if (orderStatus.equals(OrderStatus.PREPARING)) {
                return "wait till the order is prepared";
            }
        }
        return "cant receive since no order is accepted by rider.... first accept an order";
    }

    public String deliverFood() {
        if (order != null) {
            RiderAcceptance riderAcceptance = order.getRiderAcceptance();
            if (riderAcceptance.equals(RiderAcceptance.PICKED)) {
                RiderAcceptance riderAcceptance1 = riderApplication.deliverFoodToCustomer(order.getOrderID());
                this.riderStatus = RiderStatus.AVAILABLE;
                this.order = null;
                return riderAcceptance1 + " the food";
            } else if (order.getStatus().equals(OrderStatus.CANCELLED)) {
                this.order = null;
                this.riderStatus = RiderStatus.AVAILABLE;
                return "order is cancelled  ";
            }
        }
        return "cant deliver since no food picked by this rider...first accept an order";
    }
//    public String cancelOrder() {
//        if (orderList != null) {
//            RiderAcceptance riderAcceptance = riderApplication.declineOrder(orderList);
//            this.orderList = null;
//            this.riderStatus = RiderStatus.AVAILABLE;
//            return "the order is cancelled by the rider and changed rider acceptance to " + riderAcceptance;
//        }
//        return null;
//    }

    void addNotification(Notification notification) {
        if (riderStatus.equals(RiderStatus.AVAILABLE))
            this.notification.add(notification);
    }

    void setLocation(Location location) {
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
