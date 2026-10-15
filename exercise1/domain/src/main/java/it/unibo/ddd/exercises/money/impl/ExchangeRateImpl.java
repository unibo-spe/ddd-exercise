package it.unibo.ddd.exercises.money.impl;

import it.unibo.ddd.exercises.money.Currency;
import it.unibo.ddd.exercises.money.ExchangeRate;
import it.unibo.ddd.exercises.money.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

record ExchangeRateImpl(Currency source, Currency destination, BigDecimal rate, Instant when) implements ExchangeRate {
    ExchangeRateImpl {
        Objects.requireNonNull(source);
        Objects.requireNonNull(destination);
        Objects.requireNonNull(when);
        if (rate.signum() <= 0) {
            throw new IllegalArgumentException("Exchange rate must be positive: " + rate);
        }
    }

    @Override
    public Currency getSource() {
        return source;
    }

    @Override
    public Currency getDestination() {
        return destination;
    }

    @Override
    public BigDecimal getRate() {
        return rate;
    }

    @Override
    public Instant getWhen() {
        return when;
    }

    @Override
    public Money convert(Money money) {
        if (!source.equals(money.getCurrency())) {
            throw new IllegalArgumentException(money + " is not expressed in " + source.getAcronym());
        }
        return new MoneyImpl(money.getAmount().multiply(rate), destination);
    }
}
