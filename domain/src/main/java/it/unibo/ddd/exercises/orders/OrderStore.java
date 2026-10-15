package it.unibo.ddd.exercises.orders;

import it.unibo.ddd.exercises.customers.CustomerID;

import java.util.Optional;

/** Repository of {@link Order}s. */
public interface OrderStore {
    Iterable<Order> getAllOrders();

    Optional<Order> findOrderById(OrderID id);

    Iterable<Order> findOrdersByCustomer(CustomerID customer);

    void addNewOrder(Order order);

    void updateOrder(Order order);

    void removeOrder(Order order);
}
