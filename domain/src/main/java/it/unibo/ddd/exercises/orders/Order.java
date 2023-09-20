package it.unibo.ddd.exercises.orders;

import it.unibo.ddd.exercises.customers.Customer;
import it.unibo.ddd.exercises.money.Money;
import it.unibo.ddd.exercises.products.Product;
import it.unibo.ddd.exercises.products.ProductID;

import java.util.Date;
import java.util.Map;

public interface Order {
    OrderID getID();
    Customer getCustomer();
    void setCustomer(Customer customer);
    Date getWhen();
    void setWhen(Date when);
    Map<Product, Double> getQuantities();
    Money getTotalPrice();
    void addProduct(Product productID, double quantity);
}
