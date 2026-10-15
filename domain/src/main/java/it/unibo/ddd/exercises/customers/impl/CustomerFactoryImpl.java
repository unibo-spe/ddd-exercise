package it.unibo.ddd.exercises.customers.impl;

import it.unibo.ddd.exercises.customers.Customer;
import it.unibo.ddd.exercises.customers.CustomerFactory;
import it.unibo.ddd.exercises.customers.TaxCode;
import it.unibo.ddd.exercises.customers.VatNumber;

public class CustomerFactoryImpl implements CustomerFactory {
    @Override
    public TaxCode taxCode(String value) {
        return new TaxCodeImpl(value.trim().toUpperCase());
    }

    @Override
    public VatNumber vatNumber(String value) {
        return new VatNumberImpl(value.trim());
    }

    @Override
    public Customer newCustomerPerson(TaxCode code, String fullName, String email) {
        return new CustomerImpl(code, fullName, email);
    }

    @Override
    public Customer newCustomerCompany(VatNumber number, String fullName, String email) {
        return new CustomerImpl(number, fullName, email);
    }
}
