package it.unibo.ddd.exercises.money.impl;

import it.unibo.ddd.exercises.money.Currency;
import it.unibo.ddd.exercises.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

record MoneyImpl(BigDecimal amount, Currency currency) implements Money {
    MoneyImpl {
        // normalising the scale makes 10.5 and 10.50 equal
        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
        Objects.requireNonNull(currency);
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public Money plus(Money other) {
        if (!currency.equals(other.getCurrency())) {
            throw new IllegalArgumentException("Cannot sum " + this + " and " + other);
        }
        return new MoneyImpl(amount.add(other.getAmount()), currency);
    }

    @Override
    public Money times(long factor) {
        return new MoneyImpl(amount.multiply(BigDecimal.valueOf(factor)), currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency.getAcronym();
    }
}
