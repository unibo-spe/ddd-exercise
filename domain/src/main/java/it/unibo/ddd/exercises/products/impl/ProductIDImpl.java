package it.unibo.ddd.exercises.products.impl;

import it.unibo.ddd.exercises.products.ProductID;

record ProductIDImpl(String name, String type) implements ProductID {
    ProductIDImpl {
        if (name == null || name.isBlank() || type == null || type.isBlank()) {
            throw new IllegalArgumentException("Product name and type must not be blank");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }
}
