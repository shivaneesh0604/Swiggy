package library;

public interface RiderApplication {
    public RiderAcceptance acceptOrder(int orderID);
    public RiderAcceptance declineOrder(OrderList orderList);
    public Status getStatus(int orderID);
    public Status receiveOrderFromRestaurant(int orderID);
    public Status deliverFoodToCustomer(int orderID);
}
