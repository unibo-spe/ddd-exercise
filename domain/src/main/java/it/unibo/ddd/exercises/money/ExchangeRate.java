package it.unibo.ddd.exercises.money;

import java.math.BigDecimal;
import java.time.Instant;

/** Value object: how much 1 unit of the source currency is worth in the destination currency, at some moment. */
public interface ExchangeRate {
    Currency getSource();

    Currency getDestination();

    BigDecimal getRate();

    Instant getWhen();

    /** @throws IllegalArgumentException if money is not expressed in the source currency */
    Money convert(Money money);
}
