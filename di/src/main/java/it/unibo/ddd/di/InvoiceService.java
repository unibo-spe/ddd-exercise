package it.unibo.ddd.di;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

/**
 * Issues invoices for carts whose prices are expressed in EUR, converting totals in the customer's currency.
 * All dependencies are injected via the constructor: none is created here.
 */
public class InvoiceService {
    static final int PAYMENT_TERM_DAYS = 30;

    private final ExchangeRates exchangeRates;
    private final InvoiceArchive archive;
    private final Notifier notifier;
    private final Clock clock;

    public InvoiceService(ExchangeRates exchangeRates, InvoiceArchive archive, Notifier notifier, Clock clock) {
        this.exchangeRates = Objects.requireNonNull(exchangeRates);
        this.archive = Objects.requireNonNull(archive);
        this.notifier = Objects.requireNonNull(notifier);
        this.clock = Objects.requireNonNull(clock);
    }

    public Invoice issue(String customer, Map<String, BigDecimal> pricesInEur, String currency) {
        var today = LocalDate.now(clock);
        var total = pricesInEur.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (!currency.equals("EUR")) {
            total = total.multiply(exchangeRates.rate("EUR", currency, today));
        }
        var invoice = new Invoice(
                archive.getAll().size() + 1L,
                customer,
                total.setScale(2, RoundingMode.HALF_EVEN),
                currency,
                today,
                today.plusDays(PAYMENT_TERM_DAYS)
        );
        archive.store(invoice);
        notifier.invoiceIssued(invoice);
        return invoice;
    }
}
