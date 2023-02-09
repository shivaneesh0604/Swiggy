package library;

interface RiderApplication {
     void setNotificationToAnotherRider(Rider rider);
     RiderFunctionalityStatus acceptOrder(Rider rider,int orderID);
     RiderFunctionalityStatus declineOrder(Rider rider,Notification notification);
     void changeStatusByRider(Order order,RiderFunctionalityStatus riderFunctionalityStatus);
}
