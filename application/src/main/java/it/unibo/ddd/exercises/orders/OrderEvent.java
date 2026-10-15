package it.unibo.ddd.exercises.orders;

import it.unibo.ddd.exercises.customers.CustomerID;
import it.unibo.ddd.exercises.products.ProductID;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/** Domain event: immutable, it carries a snapshot of the order at the time the event occurred. */
public record OrderEvent(Kind kind, OrderID order, CustomerID customer, Instant timestamp, Map<ProductID, Long> amounts) {
    public enum Kind { PLACED, CANCELLED }

    public OrderEvent {
        Objects.requireNonNull(kind);
        Objects.requireNonNull(order);
        Objects.requireNonNull(customer);
        Objects.requireNonNull(timestamp);
        amounts = Map.copyOf(amounts);
    }

    public static OrderEvent of(Kind kind, Order order, Instant timestamp) {
        return new OrderEvent(kind, order.getID(), order.getCustomer(), timestamp, order.getAmounts());
    }
}
