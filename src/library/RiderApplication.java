package library;

import java.util.ArrayList;

public interface RiderApplication {
    public ArrayList<OrderList> showAvailableOrders();
    public OrderList acceptOrder(int orderID);
    public Status receiveOrderFromRestaurant(int orderID);
    public Status getStatus(int orderID);
    public Status deliverFoodToCustomer(int orderID);
    public RiderAcceptance deleteOrder(int orderID);
}
