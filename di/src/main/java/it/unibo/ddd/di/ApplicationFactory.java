package it.unibo.ddd.di;

import java.net.http.HttpClient;
import java.nio.file.Path;
import java.time.Clock;

/**
 * The composition root: the only place deciding which implementation to use for each interface.
 * Methods are not memoised, for the sake of simplicity: each call builds a new object graph.
 */
public class ApplicationFactory {
    private final Path archiveFile;

    public ApplicationFactory(Path archiveFile) {
        this.archiveFile = archiveFile;
    }

    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    public ExchangeRates exchangeRates() {
        return new FrankfurterExchangeRates(HttpClient.newHttpClient(), FrankfurterExchangeRates.DEFAULT_ENDPOINT);
    }

    public InvoiceArchive invoiceArchive() {
        return new FileInvoiceArchive(archiveFile);
    }

    public Notifier notifier() {
        return invoice -> System.out.println("Dear " + invoice.customer() + ", here is your invoice: " + invoice);
    }

    public InvoiceService invoiceService() {
        return new InvoiceService(exchangeRates(), invoiceArchive(), notifier(), clock());
    }
}
