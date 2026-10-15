package it.unibo.ddd.acl;

/**
 * Anti-corruption layer: CSV import/export, expressed in terms of the domain (cf. step 1 of acl/walkthrough.md).
 */
public interface CsvService {
    // TODO: add methods for writing a Table into a file, and reading a Table from a file
    //       - which settings make sense whatever the library?
    //       - which exceptions may be thrown?
    //       - no type from Apache Commons CSV or OpenCSV may appear here

    // TODO: add static factory methods, returning CsvService, so that clients never mention implementation classes
}
