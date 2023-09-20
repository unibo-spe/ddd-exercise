package it.unibo.ddd.exercises.orders;

import it.unibo.ddd.exercises.customers.Customer;

import java.util.Date;

public interface OrderFactory {
    Order createOrderFor(Customer customer, Date when);
    Order createNowOrderFor(Customer customer);
}
