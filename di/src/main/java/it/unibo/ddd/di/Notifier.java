package it.unibo.ddd.di;

@FunctionalInterface
public interface Notifier {
    void invoiceIssued(Invoice invoice);
}
