package it.unibo.ddd.exercises.money;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// TODO: remove @Disabled once the types under test exist (cf. step 2 of exercise1/walkthrough.md)
@Disabled("TODO: not implemented yet")
class MoneyTest {
    @Test
    void equalAmountsAreEqual() {
        // TODO: 10.5 EUR equals 10.50 EUR, but not 10.50 USD
        fail("TODO");
    }

    @Test
    void operationsReturnNewValues() {
        // TODO: summing or multiplying returns a new value, leaving the original one unchanged
        fail("TODO");
    }

    @Test
    void cannotSumDifferentCurrencies() {
        // TODO: summing EUR and USD fails
        fail("TODO");
    }

    @Test
    void exchangeRatesConvertFromSourceToDestination() {
        // TODO: a EUR to USD rate converts EUR amounts, and rejects USD ones
        fail("TODO");
    }
}
