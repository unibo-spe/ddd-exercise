package it.unibo.ddd.exercises.money;

public interface CurrencyFactory {
    Currency newCurrency(String name, String symbol, String acronym);
    Currency newCurrency(String name, String acronym);

    Currency eur();

    Currency usd();

    Currency gbp();

    Currency chf();

    Currency jpy();
}
