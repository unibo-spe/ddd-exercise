package it.unibo.ddd.di;

import java.util.List;

public interface InvoiceArchive {
    void store(Invoice invoice);

    List<Invoice> getAll();
}
