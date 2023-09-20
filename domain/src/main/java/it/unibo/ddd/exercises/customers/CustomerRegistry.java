package it.unibo.ddd.exercises.customers;

public interface CustomerRegistry {
    Iterable<Customer> getAllCustomers();

    Customer findCustomerById(CustomerID id);

    Iterable<Customer> findCustomerByName(String name);

    Iterable<Customer> findCustomerByEmail(String email);

    void addNewCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void removeCustomer(Customer customer);
}
