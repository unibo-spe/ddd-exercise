package it.unibo.ddd.cqrs.impl;

import it.unibo.ddd.Variation;
import it.unibo.ddd.cqrs.CounterWriter;

import java.util.LinkedList;
import java.util.List;

public class CounterWriterImpl implements CounterWriter {
    private final List<Variation> commands = new LinkedList<>();

    @Override
    public void setValue(Variation delta) {
        commands.add(delta);
    }

    public Iterable<Variation> getVariations() {
        return commands;
    }
}
