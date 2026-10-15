package it.unibo.ddd.acl;

import it.unibo.ddd.acl.impl.CommonsCsvService;
import it.unibo.ddd.acl.impl.OpenCsvService;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Anti-corruption layer: CSV import/export expressed in terms of the domain ({@link Table}s and {@link Row}s),
 * with no mention of the third-party library actually doing the job.
 */
public interface CsvService {
    char getSeparatorChar();

    char getQuoteChar();

    void write(Path path, Table table) throws IOException;

    /** The first row of the file is interpreted as headers. */
    Table read(Path path) throws IOException;

    static CsvService apacheCommons(char separator, char quote) {
        return new CommonsCsvService(separator, quote);
    }

    static CsvService openCsv(char separator, char quote) {
        return new OpenCsvService(separator, quote);
    }
}
