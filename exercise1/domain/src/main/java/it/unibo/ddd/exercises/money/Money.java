package it.unibo.ddd.exercises.money;

import java.math.BigDecimal;

/** Value object: immutable, operations return new instances. */
public interface Money {
    BigDecimal getAmount();

    Currency getCurrency();

    /** @throws IllegalArgumentException if currencies differ */
    Money plus(Money other);

    Money times(long factor);
}
