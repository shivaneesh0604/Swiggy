package library;

import java.util.HashMap;

public class Order {

    private Status status;
    private final HashMap<String, LineOrder> orders;
    private static int orderCount = 1000;
    private final int orderID;
    private final String restaurantName;
    private final int restaurantID;
    private final Location restaurantLocation;
    private final Location customerLocation;
    private final String customerID;
    private final Bill bill;
    private RiderAcceptance riderAcceptance;

    Order(String restaurantName, int restaurantID, Location restaurantLocation, Location customerLocation, String customerID) {
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

    void addOrders(LineOrder lineOrder) {
        LineOrder lineOrder1 = orders.get(lineOrder.getFoodName());
        if(lineOrder1 !=null){
            lineOrder.setQuantity(lineOrder.getQuantity()+ lineOrder.getQuantity());
        }
        this.orders.put(lineOrder.getFoodName(), lineOrder);
    }

    String deleteOrder(String foodName, int quantity) {
        LineOrder lineOrder1 = orders.get(foodName);
        if(lineOrder1 !=null){
            if (lineOrder1.getQuantity()>quantity){
                lineOrder1.setQuantity(lineOrder1.getQuantity()-quantity);
            }
            else if(lineOrder1.getQuantity()==quantity) {
                orders.remove(foodName);
                return foodName+" is totally deleted";
            }
            else if(lineOrder1.getQuantity()<quantity){
                return "cant delete since already given order is "+ lineOrder1.getQuantity();
            }
        }
        return "no order found with this foodName";
    }

    public Location getCustomerLocation() {
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

    public Location getRestaurantLocation() {
        return restaurantLocation;
    }

    public HashMap<String, LineOrder> getOrders() {
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
