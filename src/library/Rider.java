package library;

import java.util.ArrayList;

public class Rider extends User {
    private final RiderApplication applicationRaiderController;
    private RiderStatus riderStatus;
    private OrderList orderList;

    public Rider(String userID, String passWord, Application application, Role role, String name) {
        super(userID, passWord, role, name);
        this.applicationRaiderController = application;
        this.riderStatus = RiderStatus.AVAILABLE;
    }

    public ArrayList<OrderList> showAvailableOrders() {
        return applicationRaiderController.showAvailableOrders();
    }

    public String acceptOrder(int orderID) {
        Status status = applicationRaiderController.getStatus(orderID);
        if (status.equals(Status.PREPARING) || status.equals(Status.PREPARED) ) {
            this.orderList = applicationRaiderController.acceptOrder(orderID);
            this.riderStatus = RiderStatus.NOT_AVAILABLE;
            return "ACCEPTED BY " + this.getName() + " ";
        } else if (status==null) {
            return "wrong OrderID";
        }
        return "NOT ACCEPTED BY " + this.getName() + "";
    }

    public String receiveOrderFromRestaurant() {
        Status status = applicationRaiderController.getStatus(orderList.getOrderID());
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
        return "didn't receive the order yet since it is in preparation";
    }

    public String deliverFood() {
        Status status = applicationRaiderController.getStatus(orderList.getOrderID());
        if (!status.equals(Status.CANCELLED)) {
            Status status1 = applicationRaiderController.deliverFoodToCustomer(orderList.getOrderID());
            this.riderStatus = RiderStatus.AVAILABLE;
            this.orderList = null;
            return status1 + "";
        }
        return "order is cancelled  ";
    }

    public RiderStatus getRiderStatus() {
        return riderStatus;
    }
}
