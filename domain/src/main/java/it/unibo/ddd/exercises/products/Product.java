package it.unibo.ddd.exercises.products;

import it.unibo.ddd.exercises.money.Money;

public interface Product {
    ProductID getID();
    String getDescription();
    void setDescription(String description);
    Money getPrice();
    void setPrice(Money price);
    double getAvailableQuantity();
    void setAvailableQuantity(double quantity);
}
