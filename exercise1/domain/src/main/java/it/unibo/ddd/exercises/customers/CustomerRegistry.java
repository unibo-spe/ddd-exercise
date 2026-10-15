package it.unibo.ddd.exercises.customers;

import java.util.Optional;

/** Repository of {@link Customer}s. */
public interface CustomerRegistry {
    Iterable<Customer> getAllCustomers();

    Optional<Customer> findCustomerById(CustomerID id);

    Iterable<Customer> findCustomersByName(String name);

    Iterable<Customer> findCustomersByEmail(String email);

    void addNewCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void removeCustomer(Customer customer);
}
