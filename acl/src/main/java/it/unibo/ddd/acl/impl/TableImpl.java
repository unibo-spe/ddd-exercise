package it.unibo.ddd.acl.impl;

import it.unibo.ddd.acl.Row;
import it.unibo.ddd.acl.Table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class TableImpl implements Table {
    private final List<Row> rows = new ArrayList<>();
    private final boolean hasHeaders;

    public TableImpl(Row header, Iterable<Row> rows) {
        if (header != null) {
            hasHeaders = true;
            this.rows.add(header);
        } else {
            hasHeaders = false;
        }
        rows.forEach(this.rows::add);
        ensureNonEmpty();
    }

    public TableImpl(Iterable<Row> rows, boolean hasHeaders) {
        rows.forEach(this.rows::add);
        this.hasHeaders = hasHeaders;
        ensureNonEmpty();
    }

    private void ensureNonEmpty() {
        if (this.rows.isEmpty()) {
            throw new IllegalStateException("Table must have at least one row or header");
        }
    }

    @Override
    public Row getHeaders() {
        return hasHeaders ? rows.get(0) : null;
    }

    @Override
    public Iterable<Row> getEntries() {
        return rows.subList(hasHeaders ? 1 : 0, rows.size());
    }

    @Override
    public int getColumnsSize() {
        return rows.get(0).size();
    }

    @Override
    public int getRowsSize() {
        return rows.size();
    }

    @Override
    public Row getRow(int index) {
        return rows.get(index);
    }

    @Override
    public Stream<Row> stream() {
        return rows.stream();
    }

    @Override
    public Iterator<Row> iterator() {
        return rows.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Table other)) return false;
        final var i = getEntries().iterator();
        final var j = other.getEntries().iterator();
        while (i.hasNext() && j.hasNext()) {
            if (!i.next().equals(j.next())) {
                return false;
            }
        }
        return i.hasNext() == j.hasNext();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHeaders(), getEntries());
    }

    @Override
    public String toString() {
        return "Table(" +
                (hasHeaders ? "headers=" + getHeaders() + ", " : "") +
                "entries=" + getEntries() +
                ')';
    }
}
