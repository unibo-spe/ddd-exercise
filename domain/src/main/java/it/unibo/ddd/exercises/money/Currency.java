package it.unibo.ddd.exercises.money;

/** Value object, e.g. Euro, €, EUR. */
public interface Currency {
    String getName();

    String getSymbol();

    String getAcronym();
}
