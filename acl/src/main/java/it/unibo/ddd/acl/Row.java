package it.unibo.ddd.acl;

import it.unibo.ddd.acl.impl.RowImpl;

import java.util.Arrays;
import java.util.List;

public interface Row extends Iterable<String> {
    String get(int index);
    int size();
    List<String> items();

    static Row of(String... values) {
        return of(Arrays.asList(values));
    }

    static Row of(Iterable<String> values) {
        return new RowImpl(values);
    }
}
