package it.unibo.ddd.exercises.customers.impl;

import it.unibo.ddd.exercises.customers.VatNumber;

import java.util.Objects;

record VatNumberImpl(String value) implements VatNumber {
    VatNumberImpl {
        if (!Objects.requireNonNull(value).matches("\\d{11}")) {
            throw new IllegalArgumentException("Invalid VAT number: " + value);
        }
    }

    @Override
    public String getValue() {
        return value;
    }
}
