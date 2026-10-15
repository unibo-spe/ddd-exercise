package it.unibo.ddd.exercises.products;

/** Value object: products are identified by name and type/category. */
public interface ProductID {
    String getName();

    String getType();
}
