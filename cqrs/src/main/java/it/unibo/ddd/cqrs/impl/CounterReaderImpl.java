package it.unibo.ddd.cqrs.impl;

import it.unibo.ddd.Variation;
import it.unibo.ddd.cqrs.CounterReader;
import it.unibo.ddd.cqrs.CounterWriter;

import java.time.Instant;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Read model kept up to date by the variations published by the write model.
 * Its internal representation (a projection) is optimised for queries.
 */
public class CounterReaderImpl implements CounterReader {
    private final NavigableMap<Instant, Long> valuesOverTime = new TreeMap<>();

    public CounterReaderImpl(CounterWriter writer) {
        writer.getVariations().forEach(this::apply); // replay past events...
        writer.onVariation().subscribe(this::apply); // ... then follow new ones
    }

    private void apply(Variation variation) {
        valuesOverTime.put(variation.when(), variation.after());
    }

    @Override
    public long getValue() {
        return valuesOverTime.isEmpty() ? 0L : valuesOverTime.lastEntry().getValue();
    }

    @Override
    public long getValue(Instant at) {
        final var entry = valuesOverTime.floorEntry(at);
        return entry == null ? 0L : entry.getValue();
    }
}
