package it.unibo.ddd.exercises.customers;

/** Value object identifying a person, e.g. {@code RSSMRA80A01H501U}. */
public interface TaxCode extends CustomerID {
    @Override
    String getValue();
}
