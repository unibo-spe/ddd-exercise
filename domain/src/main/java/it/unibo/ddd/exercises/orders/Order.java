package it.unibo.ddd.exercises.orders;

import it.unibo.ddd.exercises.customers.CustomerID;
import it.unibo.ddd.exercises.products.ProductID;

import java.time.Instant;
import java.util.Map;

/**
 * Entity and aggregate root.
 * Customers and products are referenced by ID, as they are roots of other aggregates.
 * Any modification is forbidden once the order has been delivered.
 */
public interface Order {
    OrderID getID();

    CustomerID getCustomer();

    void setCustomer(CustomerID customer);

    Instant getTimestamp();

    void setTimestamp(Instant timestamp);

    /** @return an unmodifiable view: items can only be changed via {@link #addProduct} and {@link #removeProduct} */
    Map<ProductID, Long> getAmounts();

    /** Adds {@code amount} copies of {@code product}, on top of the ones already ordered. */
    void addProduct(ProductID product, long amount);

    void removeProduct(ProductID product);

    boolean isDelivered();

    void markDelivered();
}
