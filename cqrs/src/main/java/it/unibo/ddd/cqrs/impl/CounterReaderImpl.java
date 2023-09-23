package it.unibo.ddd.cqrs.impl;

import it.unibo.ddd.cqrs.CounterReader;
import it.unibo.ddd.cqrs.CounterWriter;

import java.util.Date;

public class CounterReaderImpl implements CounterReader {

    private final CounterWriter counterWriter;

    public CounterReaderImpl(CounterWriter counterWriter) {
        this.counterWriter = counterWriter;
    }

    @Override
    public long getValue(Date at) {
        var value = 0L;
        for (final var variation : counterWriter.getVariations()) {
            if (variation.before() != value) continue;
            if (variation.when().after(at)) return value;
            value = variation.after();
        }
        return value;
    }
}
