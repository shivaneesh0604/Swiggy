package library;

import com.sun.org.apache.xpath.internal.operations.Or;

interface RestaurantManagerApplication {
    OrderStatus setStatusPrepared(int orderID, OrderStatus orderStatus);
}
