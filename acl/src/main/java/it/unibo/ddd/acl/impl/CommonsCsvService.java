package it.unibo.ddd.acl.impl;

import it.unibo.ddd.acl.CsvService;
import it.unibo.ddd.acl.Row;
import it.unibo.ddd.acl.Table;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.stream.Collectors;

// https://www.baeldung.com/apache-commons-csv
public class CommonsCsvService implements CsvService {

    private final char separator, quote, comment;

    public CommonsCsvService(char separator, char quote, char comment) {
        this.separator = separator;
        this.quote = quote;
        this.comment = comment;
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
        return comment;
    }

    private CSVFormat getFormat() {
        return CSVFormat.DEFAULT.builder()
                .setSkipHeaderRecord(false)
                .setDelimiter(separator)
                .setQuote(quote)
                .setCommentMarker(comment)
                .build();
    }

    private Row toRow(CSVRecord record) {
        return Row.of(record.toList());
    }

    private String[] toStringArray(Row row) {
        return row.items().toArray(new String[0]);
    }

    @Override
    public void write(File path, Table table) throws IOException {
        try (Writer writer = new FileWriter(path)) {
            CSVPrinter printer = getFormat().print(writer);
            printer.printRecords(table.stream().map(this::toStringArray));
        }
    }

    @Override
    public Table read(File path) throws IOException {
        try (Reader reader = new FileReader(path)) {
            CSVParser parser = getFormat().parse(reader);
            return Table.withHeaders(
                    parser.stream().map(this::toRow).collect(Collectors.toList())
            );
        }
    }
}
