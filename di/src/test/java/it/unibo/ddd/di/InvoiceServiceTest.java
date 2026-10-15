package it.unibo.ddd.di;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Unit tests: every dependency is replaced by a test double, so results are exact and repeatable. */
class InvoiceServiceTest {
    private static final LocalDate TODAY = LocalDate.of(2026, 1, 15);
    private final Clock clock = Clock.fixed(Instant.parse("2026-01-15T10:00:00Z"), ZoneOffset.UTC);
    private final ExchangeRates rates = (from, to, date) -> {   // stub
        assertEquals(TODAY, date);
        return new BigDecimal("1.10");
    };
    private final InvoiceArchive archive = new InMemoryInvoiceArchive(); // fake
    private final Notifier notifier = mock(Notifier.class);              // mock
    private final InvoiceService service = new InvoiceService(rates, archive, notifier, clock);
    private final Map<String, BigDecimal> cart = Map.of("book", new BigDecimal("20.00"), "pen", new BigDecimal("1.50"));

    @Test
    void invoicesAreConvertedArchivedAndNotified() {
        var invoice = service.issue("Mario Rossi", cart, "USD");
        var expected = new Invoice(1, "Mario Rossi", new BigDecimal("23.65"), "USD", TODAY, LocalDate.of(2026, 2, 14));
        assertEquals(expected, invoice);
        assertEquals(List.of(expected), archive.getAll());
        verify(notifier).invoiceIssued(expected);
    }

    @Test
    void eurosAreNotConverted() {
        var service = new InvoiceService((from, to, date) -> fail("no conversion needed"), archive, notifier, clock);
        assertEquals(new BigDecimal("21.50"), service.issue("Mario Rossi", cart, "EUR").total());
    }

    @Test
    void invoicesAreNumberedProgressively() {
        service.issue("Mario Rossi", cart, "EUR");
        assertEquals(2, service.issue("Luigi Verdi", cart, "EUR").number());
    }
}
