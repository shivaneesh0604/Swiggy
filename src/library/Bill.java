package library;

import java.util.ArrayList;

public class Bill {
    public class BillItem {
        public final String itemName;
        public int quantity;
        public final double price;

        BillItem(String itemName, int quantity, double price) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.price = price;
        }

        public String getItemName() {
            return itemName;
        }

        public int getQuantity() {
            return quantity;
        }

        void setQuantity(int quantity){
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }
    }

    private final ArrayList<BillItem> items;
    private final int orderID;

    Bill(int orderID) {
        items = new ArrayList<>();
        this.orderID = orderID;
    }

    void addItem(String name, int quantity, double price) {
        items.add(new BillItem(name, quantity, price));
    }

    public int total() {
        int total = 0;
        for (BillItem item : items) {
            total += item.price * item.quantity;
        }
        return total;
    }

    public int getOrderID() {
        return orderID;
    }

    public ArrayList<BillItem> getItems() {
        return items;
    }
}
