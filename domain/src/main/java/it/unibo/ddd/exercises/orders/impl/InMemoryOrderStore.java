package it.unibo.ddd.exercises.orders.impl;

import it.unibo.ddd.exercises.customers.CustomerID;
import it.unibo.ddd.exercises.orders.Order;
import it.unibo.ddd.exercises.orders.OrderID;
import it.unibo.ddd.exercises.orders.OrderStore;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryOrderStore implements OrderStore {
    private final Map<OrderID, Order> orders = new LinkedHashMap<>();

    @Override
    public Iterable<Order> getAllOrders() {
        return List.copyOf(orders.values());
    }

    @Override
    public Optional<Order> findOrderById(OrderID id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public Iterable<Order> findOrdersByCustomer(CustomerID customer) {
        return orders.values().stream().filter(o -> o.getCustomer().equals(customer)).toList();
    }

    @Override
    public void addNewOrder(Order order) {
        if (orders.putIfAbsent(order.getID(), order) != null) {
            throw new IllegalArgumentException("Order already stored: " + order.getID());
        }
    }

    @Override
    public void updateOrder(Order order) {
        if (orders.replace(order.getID(), order) == null) {
            throw new IllegalArgumentException("No such order: " + order.getID());
        }
    }

    @Override
    public void removeOrder(Order order) {
        orders.remove(order.getID());
    }
}
