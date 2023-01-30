package library;

public interface RestaurantManagerApplication {
    Status getStatus(int orderID);
    Status setStatusPrepared(int orderID);
}
