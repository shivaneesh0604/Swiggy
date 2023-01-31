package library;

public final class ApplicationFactory {
    static CustomerApplication getCustomerApplication(){
        return new Application();
    }

    static RiderApplication getRiderApplication(){
        return new Application();
    }

    static RestaurantManagerApplication getRestaurantManagerApplication(){
        return new Application();
    }
}
