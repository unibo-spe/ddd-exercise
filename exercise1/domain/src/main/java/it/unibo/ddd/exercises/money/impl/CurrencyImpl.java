package it.unibo.ddd.exercises.money.impl;

import it.unibo.ddd.exercises.money.Currency;

import java.util.Objects;

record CurrencyImpl(String name, String symbol, String acronym) implements Currency {
    CurrencyImpl {
        Objects.requireNonNull(name);
        Objects.requireNonNull(symbol);
        if (!Objects.requireNonNull(acronym).matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Invalid currency acronym: " + acronym);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String getAcronym() {
        return acronym;
    }
}
