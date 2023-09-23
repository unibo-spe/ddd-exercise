package it.unibo.ddd.cqrs;

import it.unibo.ddd.Variation;

public interface CounterWriter {
    void setValue(Variation delta);
    Iterable<Variation> getVariations();
}
