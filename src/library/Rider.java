package library;

import java.util.ArrayList;

public final class Rider extends User {
    private final RiderApplication applicationRaiderController;
    private RiderStatus riderStatus;
    private OrderList orderList;

    public Rider(String userID, String passWord, Application application, Role role, String name) {
        super(userID, passWord, role, name);
        this.applicationRaiderController = application;
        this.riderStatus = RiderStatus.AVAILABLE;
    }

    public ArrayList<OrderList> showAvailableOrders() {
        if (riderStatus.equals(RiderStatus.NOT_AVAILABLE)) {
            return null;
        }
        return applicationRaiderController.showAvailableOrders();
    }

    public String acceptOrder(int orderID) {
        Status status = applicationRaiderController.getStatus(orderID);
        if (status == null) {
            return "wrong OrderID";
        } else if ((status.equals(Status.PREPARING) || status.equals(Status.PREPARED)) && riderStatus.equals(RiderStatus.AVAILABLE)) {
            this.orderList = applicationRaiderController.acceptOrder(orderID);
            this.riderStatus = RiderStatus.NOT_AVAILABLE;
            return "ACCEPTED BY " + this.getName() + " ";
        }
        return "NOT ACCEPTED BY " + this.getName() + "";
    }

    public String receiveOrderFromRestaurant() {
        if (orderList != null) {
            Status status = orderList.getStatus();
            if (status.equals(Status.PREPARED)) {
                Status status1 = applicationRaiderController.receiveOrderFromRestaurant(orderList.getOrderID());
                return "Status changed to " + status1;
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
                Status status1 = applicationRaiderController.deliverFoodToCustomer(orderList.getOrderID());
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

    public String deleteOrder() {
        if (orderList != null) {
            RiderAcceptance riderAcceptance = applicationRaiderController.deleteOrder(orderList.getOrderID());
            this.orderList = null;
            this.riderStatus= RiderStatus.AVAILABLE;
            return "the order is cancelled by the rider and changed rider acceptance to "+riderAcceptance;
        }
        return null;
    }

    public RiderStatus getRiderStatus() {
        return riderStatus;
    }
}
