package it.unibo.ddd.acl.impl;

import com.opencsv.*;
import it.unibo.ddd.acl.CsvService;
import it.unibo.ddd.acl.Row;
import it.unibo.ddd.acl.Table;

import java.io.*;
import java.util.LinkedList;
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
    public char getCommentChar() {
        throw new IllegalStateException("This service does not support comments");
    }

    private CSVParser buildParser() {
        return new CSVParserBuilder()
                .withSeparator(separator)
                .withQuoteChar(quote)
                .build();
    }

    private CSVWriter buildWriter(File path) throws IOException {
        return new CSVWriter(new FileWriter(path), getSeparatorChar(), getQuoteChar());
    }

    private String[] toArray(Row row) {
        return row.items().toArray(new String[0]);
    }

    @Override
    public void write(File path, Table table) throws IOException {
        try (CSVWriter writer = buildWriter(path)) {
            table.forEach(row -> writer.writeNext(toArray(row)));
        }
    }

    private CSVReader buildReader(File path) throws FileNotFoundException {
        return new CSVReaderBuilder(new FileReader(path))
                .withSkipLines(0)
                .withCSVParser(buildParser())
                .build();
    }

    @Override
    public Table read(File path) throws IOException {
        try (CSVReader reader = buildReader(path)) {
            List<Row> rows = new LinkedList<>();
            String[] line;
            while ((line = reader.readNext()) != null) {
                rows.add(Row.of(line));
            }
            return Table.withHeaders(rows);
        }
    }
}
