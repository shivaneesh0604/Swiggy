package library;

public interface RiderApplication {
    public RiderFunctionalityStatus acceptOrder(int orderID);
    public RiderFunctionalityStatus declineOrder(Order order, Rider rider,Notification notification);
    public RiderFunctionalityStatus changeStatusToPicked(int orderID);
    public RiderFunctionalityStatus changeStatusToDelivered(int orderID);
}
