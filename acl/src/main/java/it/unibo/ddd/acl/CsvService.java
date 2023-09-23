package it.unibo.ddd.acl;

import it.unibo.ddd.acl.impl.CommonsCsvService;
import it.unibo.ddd.acl.impl.OpenCsvService;

import java.io.File;
import java.io.IOException;

public interface CsvService {
    char getSeparatorChar();
    char getQuoteChar();
    char getCommentChar();

    void write(File path, Table table) throws IOException;
    Table read(File path) throws IOException;

    static CsvService apacheCommons(char separator, char quote, char comment) {
        return new CommonsCsvService(separator, quote, comment);
    }

    static OpenCsvService openCsv(char separator, char quote) {
        return new OpenCsvService(separator, quote);
    }
}
