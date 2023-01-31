package library;

import java.util.HashMap;
import java.util.Iterator;

public class OrderList {

    private Status status;
    private final HashMap<String,Order> orders;
    private static int orderCount = 1000;
    private final int orderID;
    private final String restaurantName;
    private final int restaurantID;
    private final String restaurantLocation;
    private final String customerLocation;
    private final String customerID;
    private final Bill bill;
    private RiderAcceptance riderAcceptance;

    OrderList(String restaurantName, int restaurantID, String restaurantLocation, String customerLocation, String customerID) {
        this.status = Status.PENDING;
        this.orders = new HashMap<>();
        this.restaurantID = restaurantID;
        this.restaurantName = restaurantName;
        this.restaurantLocation = restaurantLocation;
        this.customerLocation = customerLocation;
        this.customerID = customerID;
        this.riderAcceptance = RiderAcceptance.NOT_ACCEPTED;
        this.orderID = orderCount;
        this.bill = new Bill(orderID);
        orderCount++;
    }

    Bill getBill() {
        return bill;
    }

    void addOrders(Order order) {
        Order order1 = orders.get(order.getFoodName());
        if(order1!=null){
            order.setQuantity(order.getQuantity()+order.getQuantity());
        }
        this.orders.put(order.getFoodName(),order);
    }

    String deleteOrder(String foodName, int quantity) {
        Order order1 = orders.get(foodName);
        if(order1!=null){
            if (order1.getQuantity()>quantity){
                order1.setQuantity(order1.getQuantity()-quantity);
            }
            else if(order1.getQuantity()==quantity) {
                orders.remove(foodName);
                return foodName+" is totally deleted";
            }
            else if(order1.getQuantity()<quantity){
                return "cant delete since already given order is "+order1.getQuantity();
            }
        }
        return "no order found with this foodName";
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public String getRestaurantLocation() {
        return restaurantLocation;
    }

    public HashMap<String, Order> getOrders() {
        return orders;
    }

    public int getOrderID() {
        return orderID;
    }

    public Status getStatus() {
        return status;
    }

    void setStatus(Status status) {
        this.status = status;
    }

    void setRiderAcceptance(RiderAcceptance riderAcceptance) {
        this.riderAcceptance = riderAcceptance;
    }

    public RiderAcceptance getRiderAcceptance() {
        return riderAcceptance;
    }

}
