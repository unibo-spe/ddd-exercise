package it.unibo.ddd.di;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/** One invoice per line, fields separated by {@code ;}. */
public class FileInvoiceArchive implements InvoiceArchive {
    private final Path file;

    public FileInvoiceArchive(Path file) {
        this.file = Objects.requireNonNull(file);
    }

    @Override
    public void store(Invoice invoice) {
        var line = String.join(";", String.valueOf(invoice.number()), invoice.customer(), invoice.total().toPlainString(),
                invoice.currency(), invoice.issued().toString(), invoice.due().toString()) + System.lineSeparator();
        try {
            Files.writeString(file, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public List<Invoice> getAll() {
        if (!Files.exists(file)) {
            return List.of();
        }
        try (var lines = Files.lines(file)) {
            return lines.map(line -> line.split(";"))
                    .map(f -> new Invoice(Long.parseLong(f[0]), f[1], new BigDecimal(f[2]), f[3], LocalDate.parse(f[4]), LocalDate.parse(f[5])))
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
