package it.unibo.ddd.exercises.products;

import it.unibo.ddd.exercises.money.Money;

import java.util.function.Predicate;

public interface Warehouse {
    Iterable<Product> getAllProducts();

    Product findProductById(ProductID id);

    Iterable<Product> findProductByDescription(String description);

    Iterable<Product> findProductByPrice(Predicate<Money> predicate);

    void addNewProduct(Product product);

    void updateProduct(Product product);

    void removeProduct(Product product);
}
