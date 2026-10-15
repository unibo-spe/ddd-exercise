package it.unibo.ddd.cqrs.impl;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import it.unibo.ddd.Variation;
import it.unibo.ddd.cqrs.CounterWriter;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CounterWriterImpl implements CounterWriter {
    private final Clock clock;
    private final List<Variation> variations = new ArrayList<>(); // the event store
    private final Subject<Variation> onVariation = PublishSubject.create();

    public CounterWriterImpl(Clock clock) {
        this.clock = Objects.requireNonNull(clock);
    }

    public CounterWriterImpl() {
        this(Clock.systemUTC());
    }

    @Override
    public void setValue(long value) {
        final var current = variations.isEmpty() ? 0L : variations.get(variations.size() - 1).after();
        final var variation = Variation.of(current, value, clock.instant());
        variations.add(variation);
        onVariation.onNext(variation);
    }

    @Override
    public Iterable<Variation> getVariations() {
        return Collections.unmodifiableList(variations);
    }

    @Override
    public Observable<Variation> onVariation() {
        return onVariation;
    }
}
