package library;

import java.util.HashMap;

public class Order {

    private OrderStatus orderStatus;
    private final HashMap<String, LineOrder> orders;
    private static int orderCount = 1000;
    private final int orderID;
    private final String restaurantName;
    private final int restaurantID;
    private final Location restaurantLocation;
    private final Location customerLocation;
    private final String customerID;
    private final Bill bill;
    private RiderFunctionalityStatus riderFunctionalityStatus;
    private String riderName;

    Order(String restaurantName, int restaurantID, Location restaurantLocation, Location customerLocation, String customerID) {
        this.orders = new HashMap<>();
        this.restaurantID = restaurantID;
        this.restaurantName = restaurantName;
        this.restaurantLocation = restaurantLocation;
        this.customerLocation = customerLocation;
        this.customerID = customerID;
        this.riderFunctionalityStatus = RiderFunctionalityStatus.NOT_ACCEPTED;
        this.orderID = orderCount;
        this.bill = new Bill(orderID);
        orderCount++;
    }

    Bill getBill() {
        return bill;
    }

    void addOrders(Item item,int quantity) {
        LineOrder lineOrder1 = orders.get(item.getFoodName());
        if(lineOrder1!= null){
            lineOrder1.setQuantity(lineOrder1.getQuantity()+quantity);
            return;
        }
        this.orders.put(item.getFoodName(), new LineOrder(item,quantity));
    }

    OrderDeletion deleteOrder(Item item, int quantity1) {
        LineOrder lineOrder1 = orders.get(item.getFoodName());
        if(lineOrder1!=null){
            if (lineOrder1.getQuantity()>quantity1){
                lineOrder1.setQuantity(lineOrder1.getQuantity()-quantity1);
                return OrderDeletion.CHANGED_QUANTITY;
            }
            else if(lineOrder1.getQuantity()==quantity1) {
                orders.remove(item.getFoodName());
                return OrderDeletion.TOTALLY_DELETED;
            }
            else if(lineOrder1.getQuantity()<quantity1){
                return OrderDeletion.CANT_DELETE;
            }
        }
        return OrderDeletion.NO_ORDER_FOUND;
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

    public OrderStatus getStatus() {
        return orderStatus;
    }

    void setStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    void setRiderAcceptance(RiderFunctionalityStatus riderFunctionalityStatus) {
        this.riderFunctionalityStatus = riderFunctionalityStatus;
    }

    public RiderFunctionalityStatus getRiderFunctionalityStatus() {
        return riderFunctionalityStatus;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

}
