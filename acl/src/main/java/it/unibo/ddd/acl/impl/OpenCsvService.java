package it.unibo.ddd.acl.impl;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.RFC4180ParserBuilder;
import com.opencsv.exceptions.CsvValidationException;
import it.unibo.ddd.acl.CsvService;
import it.unibo.ddd.acl.Row;
import it.unibo.ddd.acl.Table;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// https://www.baeldung.com/opencsv
public class OpenCsvService implements CsvService {
    private final char separator, quote;

    public OpenCsvService(char separator, char quote) {
        this.separator = separator;
        this.quote = quote;
    }

    @Override
    public char getSeparatorChar() {
        return separator;
    }

    @Override
    public char getQuoteChar() {
        return quote;
    }

    @Override
    public void write(Path path, Table table) throws IOException {
        try (var writer = new CSVWriterBuilder(Files.newBufferedWriter(path))
                .withSeparator(separator)
                .withQuoteChar(quote)
                .build()) {
            table.forEach(row -> writer.writeNext(row.items().toArray(String[]::new)));
        }
    }

    @Override
    public Table read(Path path) throws IOException {
        // RFC 4180 parser: quotes are escaped by doubling them, as Apache Commons CSV does
        var parser = new RFC4180ParserBuilder().withSeparator(separator).withQuoteChar(quote).build();
        try (var reader = new CSVReaderBuilder(Files.newBufferedReader(path)).withCSVParser(parser).build()) {
            List<Row> rows = new ArrayList<>();
            String[] line;
            while ((line = reader.readNext()) != null) {
                rows.add(Row.of(line));
            }
            return Table.withHeaders(rows);
        } catch (CsvValidationException e) {
            // library-specific exceptions must not cross the anti-corruption layer either
            throw new IOException(e);
        }
    }
}
