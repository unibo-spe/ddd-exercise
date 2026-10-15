package it.unibo.ddd.impl;

import it.unibo.ddd.Variation;

import java.time.Instant;
import java.util.Objects;

public record VariationImpl(long before, long after, Instant when) implements Variation {
    public VariationImpl {
        Objects.requireNonNull(when);
    }
}
