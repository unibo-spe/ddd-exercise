package it.unibo.ddd.exercises.products;

import it.unibo.ddd.exercises.money.Money;

public interface ProductFactory {
    Product newProduct(String name, String type, Money price);
}
