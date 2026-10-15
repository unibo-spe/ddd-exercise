package it.unibo.ddd.exercises.customers.impl;

import it.unibo.ddd.exercises.customers.Customer;
import it.unibo.ddd.exercises.customers.CustomerID;
import it.unibo.ddd.exercises.customers.CustomerRegistry;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryCustomerRegistry implements CustomerRegistry {
    private final Map<CustomerID, Customer> customers = new LinkedHashMap<>();

    @Override
    public Iterable<Customer> getAllCustomers() {
        return List.copyOf(customers.values());
    }

    @Override
    public Optional<Customer> findCustomerById(CustomerID id) {
        return Optional.ofNullable(customers.get(id));
    }

    @Override
    public Iterable<Customer> findCustomersByName(String name) {
        return customers.values().stream().filter(c -> c.getName().equals(name)).toList();
    }

    @Override
    public Iterable<Customer> findCustomersByEmail(String email) {
        return customers.values().stream().filter(c -> c.getEmail().equals(email)).toList();
    }

    @Override
    public void addNewCustomer(Customer customer) {
        if (customers.putIfAbsent(customer.getID(), customer) != null) {
            throw new IllegalArgumentException("Customer already registered: " + customer.getID());
        }
    }

    @Override
    public void updateCustomer(Customer customer) {
        if (customers.replace(customer.getID(), customer) == null) {
            throw new IllegalArgumentException("No such customer: " + customer.getID());
        }
    }

    @Override
    public void removeCustomer(Customer customer) {
        customers.remove(customer.getID());
    }
}
