package library;

public interface RiderApplication {
    public RiderAcceptance acceptOrder(int orderID);
    public RiderAcceptance declineOrder(Order order, Rider rider);
    public OrderStatus getStatus(int orderID);
    public RiderAcceptance receiveOrderFromRestaurant(int orderID);
    public RiderAcceptance deliverFoodToCustomer(int orderID);
}
