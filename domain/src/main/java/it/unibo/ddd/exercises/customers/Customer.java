package it.unibo.ddd.exercises.customers;

public interface Customer {
    CustomerID getID();
    String getName();
    void setName(String name);

    String getEmail();
    void setEmail(String email);
}
