package it.unibo.ddd.di;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceServiceTest {
    private final Map<String, BigDecimal> cart = Map.of("book", new BigDecimal("20.00"), "pen", new BigDecimal("1.50"));

    @Test
    void invoicesInEuros() {
        var invoice = new InvoiceService().issue("Mario Rossi", cart, "EUR");
        assertEquals(new BigDecimal("21.50"), invoice.total());
        assertEquals(LocalDate.now(), invoice.issued()); // what if the test runs across midnight?
        // assertEquals(1, invoice.number()); // fails from the 2nd run on: why?
        assertTrue(invoice.number() >= 1);
    }

    @Test
    void invoicesInDollars() {
        // requires the Internet, and rates change every day: the exact total cannot be asserted
        var invoice = new InvoiceService().issue("Mario Rossi", cart, "USD");
        assertTrue(invoice.total().signum() > 0);
    }
}
