package library;

public interface RiderApplication {
    public RiderAcceptance acceptOrder(int orderID);
    public RiderAcceptance declineOrder(Order order, Rider rider);
    public Status getStatus(int orderID);
    public Status receiveOrderFromRestaurant(int orderID);
    public Status deliverFoodToCustomer(int orderID);
}
