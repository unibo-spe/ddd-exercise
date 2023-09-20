package it.unibo.ddd.exercises.customers;

import java.math.BigInteger;

public interface VatNumber extends CustomerID {
    @Override
    BigInteger getValue();
}
