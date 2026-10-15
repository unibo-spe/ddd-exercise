package it.unibo.ddd.exercises.customers.impl;

import it.unibo.ddd.exercises.customers.TaxCode;

import java.util.Objects;
import java.util.regex.Pattern;

record TaxCodeImpl(String value) implements TaxCode {
    private static final Pattern FORMAT = Pattern.compile("[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]");

    TaxCodeImpl {
        if (!FORMAT.matcher(Objects.requireNonNull(value)).matches()) {
            throw new IllegalArgumentException("Invalid tax code: " + value);
        }
    }

    @Override
    public String getValue() {
        return value;
    }
}
