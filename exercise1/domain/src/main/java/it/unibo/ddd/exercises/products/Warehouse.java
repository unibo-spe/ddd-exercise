package it.unibo.ddd.exercises.products;

import java.util.Optional;

/** Repository of {@link Product}s. */
public interface Warehouse {
    Iterable<Product> getAllProducts();

    Optional<Product> findProductById(ProductID id);

    Iterable<Product> findProductsByType(String type);

    void addNewProduct(Product product);

    void updateProduct(Product product);

    void removeProduct(Product product);
}
