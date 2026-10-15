package it.unibo.ddd.exercises.products.impl;

import it.unibo.ddd.exercises.money.Money;
import it.unibo.ddd.exercises.products.Product;
import it.unibo.ddd.exercises.products.ProductID;

import java.util.Objects;

class ProductImpl implements Product {
    private final ProductID id;
    private Money price;
    private long availableQuantity;

    ProductImpl(ProductID id, Money price, long availableQuantity) {
        this.id = Objects.requireNonNull(id);
        setPrice(price);
        setAvailableQuantity(availableQuantity);
    }

    @Override
    public ProductID getID() {
        return id;
    }

    @Override
    public Money getPrice() {
        return price;
    }

    @Override
    public void setPrice(Money price) {
        if (price.getAmount().signum() < 0) {
            throw new IllegalArgumentException("Price must not be negative: " + price);
        }
        this.price = price;
    }

    @Override
    public long getAvailableQuantity() {
        return availableQuantity;
    }

    @Override
    public void setAvailableQuantity(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Available quantity must not be negative: " + quantity);
        }
        this.availableQuantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Product other && id.equals(other.getID());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Product(" + id + ", " + price + ", available=" + availableQuantity + ")";
    }
}
