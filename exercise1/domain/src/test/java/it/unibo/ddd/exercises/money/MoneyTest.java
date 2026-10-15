package it.unibo.ddd.exercises.money;

import it.unibo.ddd.exercises.money.impl.MoneyFactoryImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {
    private final MoneyFactory factory = new MoneyFactoryImpl();
    private final Currency eur = factory.eur();
    private final Currency usd = factory.usd();

    private Money eur(String amount) {
        return factory.money(new BigDecimal(amount), eur);
    }

    @Test
    void moneyIsAValueObject() {
        assertEquals(eur("10.5"), eur("10.50"));
        assertNotEquals(eur("10.50"), factory.money(new BigDecimal("10.50"), usd));
    }

    @Test
    void operationsReturnNewValues() {
        var ten = eur("10");
        assertEquals(eur("12.50"), ten.plus(eur("2.5")));
        assertEquals(eur("30"), ten.times(3));
        assertEquals(eur("10"), ten);
    }

    @Test
    void cannotSumDifferentCurrencies() {
        assertThrows(IllegalArgumentException.class, () -> eur("1").plus(factory.money(BigDecimal.ONE, usd)));
    }

    @Test
    void exchangeRatesConvertFromSourceToDestination() {
        var rate = factory.exchangeRate(eur, usd, new BigDecimal("1.10"), Instant.EPOCH);
        assertEquals(factory.money(new BigDecimal("11"), usd), rate.convert(eur("10")));
        assertThrows(IllegalArgumentException.class, () -> rate.convert(factory.money(BigDecimal.ONE, usd)));
    }
}
