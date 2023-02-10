package library;

interface RiderApplication {
     void setNotification(Rider rider);
     RiderFunctionalityStatus acceptOrder(Rider rider,int orderID);
     RiderFunctionalityStatus declineOrder(Rider rider,Notification notification);
     void changeStatusByRider(Order order,RiderFunctionalityStatus riderFunctionalityStatus);
}
