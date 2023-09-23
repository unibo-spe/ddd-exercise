package it.unibo.ddd;

import it.unibo.ddd.impl.VariationImpl;

import java.util.Date;

public interface Variation {
    long before();
    long after();
    default long delta() { return after() - before(); }
    Date when();

    public static Variation of(long before, long after, Date when) {
        return new VariationImpl(before, after, when);
    }

    public static Variation of(long before, long after) {
        return of(before, after, new Date());
    }
}
