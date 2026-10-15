package it.unibo.ddd.exercises.money.impl;

import it.unibo.ddd.exercises.money.Currency;
import it.unibo.ddd.exercises.money.ExchangeRate;
import it.unibo.ddd.exercises.money.Money;
import it.unibo.ddd.exercises.money.MoneyFactory;

import java.math.BigDecimal;
import java.time.Instant;

public class MoneyFactoryImpl implements MoneyFactory {
    @Override
    public Currency currency(String name, String symbol, String acronym) {
        return new CurrencyImpl(name, symbol, acronym);
    }

    @Override
    public Currency eur() {
        return currency("Euro", "€", "EUR");
    }

    @Override
    public Currency usd() {
        return currency("US Dollar", "$", "USD");
    }

    @Override
    public Currency gbp() {
        return currency("Pound Sterling", "£", "GBP");
    }

    @Override
    public Money money(BigDecimal amount, Currency currency) {
        return new MoneyImpl(amount, currency);
    }

    @Override
    public ExchangeRate exchangeRate(Currency source, Currency destination, BigDecimal rate, Instant when) {
        return new ExchangeRateImpl(source, destination, rate, when);
    }
}
