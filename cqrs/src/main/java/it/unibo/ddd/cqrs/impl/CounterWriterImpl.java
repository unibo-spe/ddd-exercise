package it.unibo.ddd.cqrs.impl;

import io.reactivex.rxjava3.core.Observable;
import it.unibo.ddd.Variation;
import it.unibo.ddd.cqrs.CounterWriter;

import java.time.Clock;
import java.util.Objects;

/** Write model (cf. steps 1-2 of cqrs/walkthrough.md). */
public class CounterWriterImpl implements CounterWriter {
    private final Clock clock;

    // TODO: add the event store, i.e. the list of variations, and a Subject to publish them (cf. CounterImpl)
    //       notice that there should be no field storing the current value

    public CounterWriterImpl(Clock clock) {
        this.clock = Objects.requireNonNull(clock);
    }

    public CounterWriterImpl() {
        this(Clock.systemUTC());
    }

    @Override
    public void setValue(long value) {
        // TODO: compute the current value out of the last variation (or 0), then store and publish a new variation
        throw new UnsupportedOperationException("TODO: not implemented yet");
    }

    @Override
    public Iterable<Variation> getVariations() {
        // TODO: return the whole history, without letting callers modify it
        throw new UnsupportedOperationException("TODO: not implemented yet");
    }

    @Override
    public Observable<Variation> onVariation() {
        throw new UnsupportedOperationException("TODO: not implemented yet");
    }
}
