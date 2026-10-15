package it.unibo.ddd.exercises.customers;

/** Entity (and aggregate root): identified by its {@link CustomerID}, name and email may vary over time. */
public interface Customer {
    CustomerID getID();

    String getName();

    void setName(String name);

    String getEmail();

    void setEmail(String email);
}
