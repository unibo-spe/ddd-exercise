package it.unibo.ddd.exercises.customers.impl;

import it.unibo.ddd.exercises.customers.Customer;
import it.unibo.ddd.exercises.customers.CustomerID;

import java.util.Objects;

class CustomerImpl implements Customer {
    private final CustomerID id;
    private String name;
    private String email;

    CustomerImpl(CustomerID id, String name, String email) {
        this.id = Objects.requireNonNull(id);
        setName(name);
        setEmail(email);
    }

    @Override
    public CustomerID getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Customer name must not be blank");
        }
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        if (email == null || !email.matches("[^@\\s]+@[^@\\s]+")) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
        this.email = email;
    }

    // entities are equal iff their identifiers are equal
    @Override
    public boolean equals(Object o) {
        return o instanceof Customer other && id.equals(other.getID());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Customer(" + id.getValue() + ", " + name + ", " + email + ")";
    }
}
