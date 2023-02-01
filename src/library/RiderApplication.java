package library;

interface RiderApplication {
     RiderFunctionalityStatus acceptOrder(int orderID);
     RiderFunctionalityStatus declineOrder(Order order, Rider rider,Notification notification);
     RiderFunctionalityStatus changeStatusToPicked(int orderID);
     RiderFunctionalityStatus changeStatusToDelivered(int orderID);
}
