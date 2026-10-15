package it.unibo.ddd.exercises.products.impl;

import it.unibo.ddd.exercises.money.Money;
import it.unibo.ddd.exercises.products.Product;
import it.unibo.ddd.exercises.products.ProductFactory;
import it.unibo.ddd.exercises.products.ProductID;

public class ProductFactoryImpl implements ProductFactory {
    @Override
    public ProductID productID(String name, String type) {
        return new ProductIDImpl(name, type);
    }

    @Override
    public Product newProduct(ProductID id, Money price, long availableQuantity) {
        return new ProductImpl(id, price, availableQuantity);
    }
}
