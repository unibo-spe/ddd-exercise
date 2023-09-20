package it.unibo.ddd.exercises.money;

import java.util.Date;

public interface CurrencyExchangeService {
    ExchangeRate GetCurrentExchangeRateAmong(Currency source, Currency destination);

    ExchangeRate GetExchangeRateAmong(Currency source, Currency destination, Date when);
}
