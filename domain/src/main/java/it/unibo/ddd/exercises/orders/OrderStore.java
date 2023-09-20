package it.unibo.ddd.exercises.orders;

import it.unibo.ddd.exercises.customers.Customer;

import java.util.Date;
import java.util.function.Predicate;

public interface OrderStore {
    Iterable<Order> getAllOrders();

    Order findOrderById(OrderID id);

    Iterable<Order> findOrderByCustomer(Predicate<Customer> predicate);

    Iterable<Order> findOrderByTime(Predicate<Date> predicate);

    void addNewOrder(Order order);

    void updateOrder(Order order);

    void RemoveOrder(Order order);
}
