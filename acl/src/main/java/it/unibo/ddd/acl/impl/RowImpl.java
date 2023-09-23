package it.unibo.ddd.acl.impl;

import it.unibo.ddd.acl.Row;

import java.util.*;
import java.util.stream.Collectors;

public class RowImpl implements Row {
    private final List<String> items = new ArrayList<>();

    public RowImpl(Iterable<String> items) {
        items.forEach(this.items::add);
    }

    @Override
    public String get(int index) {
        return items.get(index);
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public List<String> items() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public Iterator<String> iterator() {
        return items.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Row strings)) return false;
        return Objects.equals(items, strings.items());
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    @Override
    public String toString() {
        return "Row(" + String.join(", ", items) + ')';
    }
}
