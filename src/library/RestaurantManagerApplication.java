package library;

public interface RestaurantManagerApplication {
    Status getStatus(int orderID);
    Status setStatusPREPARED(int orderID);
}
