package it.unibo.ddd.exercises.orders;

import it.unibo.ddd.exercises.customers.CustomerID;

public interface OrderFactory {
    /** Creates an empty order, with a fresh serial number, performed now. */
    Order newOrder(CustomerID customer);
}
