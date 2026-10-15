package it.unibo.ddd.acl.impl;

import it.unibo.ddd.acl.CsvService;
import it.unibo.ddd.acl.Row;
import it.unibo.ddd.acl.Table;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// https://www.baeldung.com/apache-commons-csv
public class CommonsCsvService implements CsvService {
    private final CSVFormat format;

    public CommonsCsvService(char separator, char quote) {
        this.format = CSVFormat.DEFAULT.builder()
                .setDelimiter(separator)
                .setQuote(quote)
                .get();
    }

    @Override
    public char getSeparatorChar() {
        return format.getDelimiterString().charAt(0);
    }

    @Override
    public char getQuoteChar() {
        return format.getQuoteCharacter();
    }

    @Override
    public void write(Path path, Table table) throws IOException {
        try (var printer = format.print(Files.newBufferedWriter(path))) {
            for (Row row : table) {
                printer.printRecord(row.items());
            }
        }
    }

    @Override
    public Table read(Path path) throws IOException {
        try (var parser = format.parse(Files.newBufferedReader(path))) {
            return Table.withHeaders(parser.stream().map(CSVRecord::toList).map(Row::of).toList());
        }
    }
}
