package it.unibo.ddd.exercises.money;

import java.math.BigDecimal;
import java.time.Instant;

/** Factory for all value objects of the money context. */
public interface MoneyFactory {
    Currency currency(String name, String symbol, String acronym);

    Currency eur();

    Currency usd();

    Currency gbp();

    Money money(BigDecimal amount, Currency currency);

    ExchangeRate exchangeRate(Currency source, Currency destination, BigDecimal rate, Instant when);
}
