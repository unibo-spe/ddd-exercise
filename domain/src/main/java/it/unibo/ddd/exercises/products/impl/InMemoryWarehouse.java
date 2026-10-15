package it.unibo.ddd.exercises.products.impl;

import it.unibo.ddd.exercises.products.Product;
import it.unibo.ddd.exercises.products.ProductID;
import it.unibo.ddd.exercises.products.Warehouse;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryWarehouse implements Warehouse {
    private final Map<ProductID, Product> products = new LinkedHashMap<>();

    @Override
    public Iterable<Product> getAllProducts() {
        return List.copyOf(products.values());
    }

    @Override
    public Optional<Product> findProductById(ProductID id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public Iterable<Product> findProductsByType(String type) {
        return products.values().stream().filter(p -> p.getID().getType().equals(type)).toList();
    }

    @Override
    public void addNewProduct(Product product) {
        if (products.putIfAbsent(product.getID(), product) != null) {
            throw new IllegalArgumentException("Product already stored: " + product.getID());
        }
    }

    @Override
    public void updateProduct(Product product) {
        if (products.replace(product.getID(), product) == null) {
            throw new IllegalArgumentException("No such product: " + product.getID());
        }
    }

    @Override
    public void removeProduct(Product product) {
        products.remove(product.getID());
    }
}
