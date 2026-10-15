package it.unibo.ddd.exercises.customers;

/** Value object identifying a company, i.e. 11 digits (leading zeros matter). */
public interface VatNumber extends CustomerID {
    @Override
    String getValue();
}
