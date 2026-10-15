package it.unibo.ddd.di;

import java.math.BigDecimal;
import java.time.LocalDate;

/** How much 1 unit of {@code from} is worth in {@code to}, on some day. */
@FunctionalInterface
public interface ExchangeRates {
    BigDecimal rate(String from, String to, LocalDate date);
}
