package it.unibo.ddd;

import it.unibo.ddd.impl.VariationImpl;

import java.time.Instant;

/** Domain event: the counter changed from {@link #before()} to {@link #after()} at {@link #when()}. */
public interface Variation {
    long before();

    long after();

    default long delta() {
        return after() - before();
    }

    Instant when();

    static Variation of(long before, long after, Instant when) {
        return new VariationImpl(before, after, when);
    }
}
