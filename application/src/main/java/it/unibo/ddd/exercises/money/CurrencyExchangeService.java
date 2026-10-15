package it.unibo.ddd.exercises.money;

import java.time.Instant;

/**
 * Service providing exchange rates, e.g. by querying some Web API.
 * Implementations are adapters towards the outside world, hence they do not belong to the domain.
 */
@FunctionalInterface
public interface CurrencyExchangeService {
    ExchangeRate getExchangeRate(Currency source, Currency destination, Instant when);
}
