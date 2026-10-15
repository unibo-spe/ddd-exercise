package it.unibo.ddd.exercises.products;

import it.unibo.ddd.exercises.money.Money;

public interface ProductFactory {
    ProductID productID(String name, String type);

    Product newProduct(ProductID id, Money price, long availableQuantity);
}
