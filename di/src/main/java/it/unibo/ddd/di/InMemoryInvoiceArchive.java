package it.unibo.ddd.di;

import java.util.ArrayList;
import java.util.List;

public class InMemoryInvoiceArchive implements InvoiceArchive {
    private final List<Invoice> invoices = new ArrayList<>();

    @Override
    public void store(Invoice invoice) {
        invoices.add(invoice);
    }

    @Override
    public List<Invoice> getAll() {
        return List.copyOf(invoices);
    }
}
