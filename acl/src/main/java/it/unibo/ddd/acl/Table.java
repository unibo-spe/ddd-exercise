package it.unibo.ddd.acl;

import it.unibo.ddd.acl.impl.TableImpl;

import java.util.List;
import java.util.stream.Stream;

public interface Table extends Iterable<Row> {
    Row getHeaders();
    Iterable<Row> getEntries();
    int getColumnsSize();
    int getRowsSize();
    Row getRow(int index);
    Stream<Row> stream();

    static Table withHeaders(Row headers, Iterable<Row> rows) {
        return new TableImpl(headers, rows);
    }

    static Table withHeaders(Row headers, Row... rows) {
        return withHeaders(headers, List.of(rows));
    }

    static Table withHeaders(Iterable<Row> rows) {
        return new TableImpl(rows, true);
    }

    static Table withoutHeaders(Iterable<Row> rows) {
        return new TableImpl(rows, false);
    }

    static Table withoutHeaders(Row... rows) {
        return withoutHeaders(List.of(rows));
    }
}
