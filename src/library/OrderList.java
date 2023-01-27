package library;

import java.util.ArrayList;
import java.util.Iterator;

public class OrderList {

    private Status status;
    private final ArrayList<Order> orders;
    private int orderID;
    private final String restaurantName;
    private final int restaurantID;
    private final String restaurantLocation;
    private final String customerLocation;
    private final String customerID;
    private final Bill bill;

    private RiderAcceptance riderAcceptance;

    public OrderList(String restaurantName, int restaurantID, String restaurantLocation, String customerLocation, String customerID) {
        this.status = Status.PENDING;
        this.orders = new ArrayList<>();
        this.restaurantID = restaurantID;
        this.restaurantName = restaurantName;
        this.restaurantLocation = restaurantLocation;
        this.customerLocation = customerLocation;
        this.customerID = customerID;
        this.bill = new Bill(orderID);
        this.riderAcceptance = RiderAcceptance.NOT_ACCEPTED;
    }

    Bill getBill() {
        return bill;
    }

    void addOrders(Order order, double price) {
        this.orders.add(order);
    }

    String deleteOrder(String foodName, int quantity) {
        Iterator<Order> it = orders.iterator();
        while (it.hasNext()) {
            Order order = it.next();
            if (order.getFoodName().equals(foodName.toUpperCase())) {
                if (order.getQuantity() == quantity) {
                    it.remove();
                    return order.getFoodName() + " is totally deleted";
                } else if (order.getQuantity() > quantity) {
                    order.setQuantity(order.getQuantity() - quantity);
                    return "Changed order is " + order.getFoodName() + " with the quantity " + order.getQuantity();
                } else if (order.getQuantity() < quantity) {
                    return "Your order is not compatible to change please enter value on or below "
                            + order.getQuantity() + " to delete";
                }
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

    public ArrayList<Order> getOrders() {
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

     void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    void setRiderAcceptance(RiderAcceptance riderAcceptance) {
        this.riderAcceptance = riderAcceptance;
    }

    public RiderAcceptance getRiderAcceptance() {
        return riderAcceptance;
    }

}
