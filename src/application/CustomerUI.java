package application;

import library.Customer;
import library.Database;

import java.util.HashMap;

public class CustomerUI implements UI{
    private final Customer customer ;
    public CustomerUI(Customer customer) {
        this.customer = customer;
    }
    public void entersUI(){
        HashMap<Integer,String> listOfRestaurants = Database.getInstance().getAllRestaurant();

    }
}