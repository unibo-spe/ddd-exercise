package it.unibo.ddd.exercises.orders.impl;

import it.unibo.ddd.exercises.customers.CustomerID;
import it.unibo.ddd.exercises.orders.Order;
import it.unibo.ddd.exercises.orders.OrderID;
import it.unibo.ddd.exercises.products.ProductID;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

class OrderImpl implements Order {
    private final OrderID id;
    private CustomerID customer;
    private Instant timestamp;
    private final Map<ProductID, Long> amounts = new LinkedHashMap<>();
    private boolean delivered = false;

    OrderImpl(OrderID id, CustomerID customer, Instant timestamp) {
        this.id = Objects.requireNonNull(id);
        this.customer = Objects.requireNonNull(customer);
        this.timestamp = Objects.requireNonNull(timestamp);
    }

    private void ensureNotDelivered() {
        if (delivered) {
            throw new IllegalStateException("Order " + id.getSerialNumber() + " has already been delivered");
        }
    }

    @Override
    public OrderID getID() {
        return id;
    }

    @Override
    public CustomerID getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(CustomerID customer) {
        ensureNotDelivered();
        this.customer = Objects.requireNonNull(customer);
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Instant timestamp) {
        ensureNotDelivered();
        this.timestamp = Objects.requireNonNull(timestamp);
    }

    @Override
    public Map<ProductID, Long> getAmounts() {
        return Collections.unmodifiableMap(amounts);
    }

    @Override
    public void addProduct(ProductID product, long amount) {
        ensureNotDelivered();
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive: " + amount);
        }
        amounts.merge(Objects.requireNonNull(product), amount, Long::sum);
    }

    @Override
    public void removeProduct(ProductID product) {
        ensureNotDelivered();
        amounts.remove(product);
    }

    @Override
    public boolean isDelivered() {
        return delivered;
    }

    @Override
    public void markDelivered() {
        delivered = true;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Order other && id.equals(other.getID());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Order(" + id.getSerialNumber() + ", " + customer.getValue() + ", " + timestamp + ", " + amounts + ")";
    }
}
