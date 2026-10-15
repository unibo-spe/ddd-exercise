package it.unibo.ddd.exercises.products;

import it.unibo.ddd.exercises.money.Money;

/** Entity (and aggregate root): price and availability may vary over time. */
public interface Product {
    ProductID getID();

    Money getPrice();

    void setPrice(Money price);

    long getAvailableQuantity();

    void setAvailableQuantity(long quantity);
}
